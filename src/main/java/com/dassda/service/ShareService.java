package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ShareRepository;
import com.dassda.request.ShareRequest;
import com.dassda.response.InviteInfoResponse;
import com.dassda.response.ShareResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ShareRepository shareRepository;


    private Member member() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(() -> new IllegalStateException("다시 로그인"));
    }
    public ShareResponse createInvitation(Long pathBoardId, ShareRequest request) throws NoSuchAlgorithmException, IllegalArgumentException {
        // 요청 본문의 boardId와 URL 경로의 boardId 일치 여부 확인
        if (!pathBoardId.equals(request.getBoardId())) {
            throw new IllegalArgumentException("URL 경로의 boardId와 요청 본문의 boardId가 일치하지 않습니다.");
        }

        // boardId 유효성 확인
        Board board = boardRepository.findById(pathBoardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + pathBoardId));

        // 이미 저장된 공유 링크가 있는지 확인
        if (board.getShareLinkHash() != null && !board.getShareLinkHash().isEmpty()) {
            // 기존 링크 재사용
            return new ShareResponse("http://localhost:8080/share?hash=" + board.getShareLinkHash());
        } else {
            // 새 공유 링크 생성
            String hashValue = generateHash(request, LocalDateTime.now());
            board.setShareLinkHash(hashValue);
            boardRepository.save(board);

            return new ShareResponse("http://localhost:8080/share?hash=" + hashValue);
        }
    }

    private String generateHash(ShareRequest request, LocalDateTime timestamp) throws NoSuchAlgorithmException {

        // 요청 정보와 타임스탬프를 사용하여 고유한 해시 생성
        String input = String.format("%s|%s|%d|%s|%s",
                request.getNickname(),
                request.getProfileUrl(),
                request.getBoardId(),
                request.getTitle(),
                timestamp.toString());
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    @Transactional
    public InviteInfoResponse processSharedAccessByHash(String hashValue) {
        try {
            System.out.println("Processing shared access by hash: " + hashValue);
            // 해시값으로 Board 조회
            Board board = boardRepository.findByShareLinkHash(hashValue)
                    .orElseThrow(() -> new EntityNotFoundException("해당 해시값에 유효한 보드가 없습니다."));

            // 연관된 Member(초대한 사람) 정보 조회
            Member inviter = board.getMember();

            // ShareResponse 객체 생성하여 반환
            return new InviteInfoResponse(inviter.getNickname(), inviter.getProfile_image_url(), board.getId(), board.getTitle());
        } catch (EntityNotFoundException e) {
            System.out.println("EntityNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void joinDiary(Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        Share share = new Share();
        share.setMember(member());
        share.setBoard(boardOptional.get());
        share.setRegDate(LocalDateTime.now());
        shareRepository.save(share);
    }
}
