package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ShareRepository;
import com.dassda.request.ShareRequest;
import com.dassda.response.ShareResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    //초대 생성 로직
    public ShareResponse createInvitation(Long boardId, ShareRequest request) throws NoSuchAlgorithmException {
        //특정 일기장에 대한 기존 초대링크 학인
        Optional<Share> existingShare = shareRepository.findByBoardIdAndExpiryDateAfter(boardId);
        ShareResponse shareResponse = new ShareResponse();

        Share share;
        String hashValue;
        if (existingShare.isPresent()) {
            share = existingShare.get();
            shareResponse.setShareLink("http://localhost:8080/share?hash=" + share.getHashValue());
            return shareResponse;
        } else {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));

            Member member = memberRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + request.getMemberId()));

            //현재 시간 +7일로 만료 날짜 설정
            LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);
            //해시값 생성을 위한 입력 데이터 조합
            String hashInput = String.format("%s|%s|%d|%s|%s", request.getName(), request.getProfileImageUrl(), boardId, request.getTitle(), expiryDate);
            //해시값 생성
            hashValue = generateHash(hashInput);

            share = new Share();
            share.setHashValue(hashValue);
            share.setBoard(board);
            share.setMember(member);

            shareRepository.save(share);
            shareResponse.setShareLink("http://localhost:8080/share?hash=" + hashValue);
            return shareResponse;
        }
    }

    private String generateHash(String input) throws NoSuchAlgorithmException {
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



//
//    public boolean isInvitedUser(Long currentUserId, String hashValue) {
//        Share share = shareRepository.findByHashValue(hashValue)
//                .orElseThrow(() -> new EntityNotFoundException("Invitation not found with hash value: " + hashValue));
//        return share.getMember().getId().equals(currentUserId);
//    }
//
//    public ResponseEntity<?> processSharedAccess(Long boardId, String hash, Authentication authentication) {
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            String currentUsername = authentication.getName();
//            Member currentMember = memberRepository.findByEmail(currentUsername)
//                    .orElseThrow(() -> new EntityNotFoundException("Member not found with username: " + currentUsername));
//            Long currentUserId = currentMember.getId();
//
//            try {
//                Board board = this.acceptAndRetrieve(boardId, hash, currentUserId);
//
//            }


}
