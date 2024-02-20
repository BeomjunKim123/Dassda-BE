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
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));
        if (!board.getMember().getId().equals(member().getId())) {
            throw new IllegalStateException("일기장 소유자가 아니다");
        }
        if (board.getShareLinkHash() != null && !board.getShareLinkHash().isEmpty()) {
            return new ShareResponse(board.getShareLinkHash());
        } else {
            String hashValue = generateHash(boardId, LocalDateTime.now(), board);
            board.setShareLinkHash(hashValue);
            boardRepository.save(board);

            return new ShareResponse(hashValue);
        }
    }

    private String generateHash(Long boardId, LocalDateTime timestamp, Board board) throws NoSuchAlgorithmException {
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

        for (int i = 0; i < hash.length && i < 16; i++) {
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
            Board board = boardRepository.findByShareLinkHash(hashValue)
                    .orElseThrow(() -> new EntityNotFoundException("해당 해시값에 유효한 보드가 없습니다."));
            Member inviter = board.getMember();
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
