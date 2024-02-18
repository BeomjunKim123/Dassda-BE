package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.event.NewMemberEvent;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ShareRepository;
import com.dassda.request.ShareRequest;
import com.dassda.response.InviteInfoResponse;
import com.dassda.response.ShareResponse;
import com.dassda.utils.GetMember;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ShareRepository shareRepository;
    private final ApplicationEventPublisher eventPublisher;
    private Member member() {
        return GetMember.getCurrentMember();
    }
    public ShareResponse createInvitation(Long boardId) throws NoSuchAlgorithmException, IllegalArgumentException {
        // 요청 본문의 boardId와 URL 경로의 boardId 일치 여부 확인
//        if (!pathBoardId.equals(pathBoardId)) {
//            throw new IllegalArgumentException("URL 경로의 boardId와 요청 본문의 boardId가 일치하지 않습니다.");
//        }

        // boardId 유효성 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));
        if (!board.getMember().getId().equals(member().getId())) {
            throw new IllegalStateException("일기장 소유자가 아니다");
        }
        // 이미 저장된 공유 링크가 있는지 확인
        if (board.getShareLinkHash() != null && !board.getShareLinkHash().isEmpty()) {
            // 기존 링크 재사용
            return new ShareResponse("http://localhost:8080/api/boards/share/" + board.getShareLinkHash());
        } else {
            // 새 공유 링크 생성
            String hashValue = generateHash(boardId, LocalDateTime.now(), board);
            board.setShareLinkHash(hashValue);
            boardRepository.save(board);

            return new ShareResponse("http://localhost:8080/api/boards/share/" + hashValue);
        }
    }

    private String generateHash(Long boardId, LocalDateTime timestamp, Board board) throws NoSuchAlgorithmException {

        // 요청 정보와 타임스탬프를 사용하여 고유한 해시 생성
        String input = String.format("%s|%s|%d|%s|%s",
                board.getMember().getNickname(),
                board.getMember().getProfile_image_url(),
                boardId,
                board.getTitle(),
                board.getMember().getId(),
                timestamp.toString());
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());

        Formatter formatter = new Formatter();

        for (int i = 0; i < hash.length && i < 16; i++) { // 16바이트(32자리 16진수 문자열)만 사용
            formatter.format("%02x", hash[i]);
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
        if(!shareRepository.existsByBoardIdAndMemberId(boardId, member().getId())) {
            Share share = new Share();
            share.setMember(member());
            share.setBoard(boardOptional.get());
            share.setRegDate(LocalDateTime.now());
            shareRepository.save(share);

            boardOptional.get().setShared(true);
            boardRepository.save(boardOptional.get());

            eventPublisher.publishEvent(new NewMemberEvent(this, share));
        } else {
            throw new IllegalArgumentException("이미 참여한 일기장입니다");
        }

    }
}
