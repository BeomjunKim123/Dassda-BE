package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ShareRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Formatter;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ShareRepository shareRepository;

    //초대 생성 로직
    public Share createInvitation(Long boardId, Long memberId, String name, String profileImageUrl, String title) throws NoSuchAlgorithmException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        //현재 시간 +7일로 만료 날짜 설정
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);
        //해시값 생성을 위한 입력 데이터 조합
        String hashInput = String.format("%s|%s|%d|%s|%s", name, profileImageUrl, boardId, title, expiryDate);
        //해시값 생성
        String hashValue = generateHash(hashInput);

        Share share = new Share();
        share.setHashValue(hashValue);
        share.setBoard(board);
        share.setMember(member);
        share.setExpiryDate(expiryDate);

        //초대 링크 생성 (여기서는 해값만을 사용하지만, 실제로는 이 값을 URL의 파라미터로 사용)
        String invitationLink = "https://yourapp.com/invite?hash=" + hashValue;
        //초대 링크 저장
        //invitation.setInvitationLink(invitationLink);

        return shareRepository.save(share);
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

    //초대 링크 접근 처리
    public Board accessInvitation(String hashValue) {
        Share share = shareRepository.findByHashValue(hashValue)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found with hash value: " + hashValue));

        if(share.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new EntityNotFoundException("Invitation Link is expired");
        }
        return share.getBoard();
    }


    public boolean isInvitedUser(Long currentUserId, String hashValue) {
        Share share = shareRepository.findByHashValue(hashValue)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found with hash value: " + hashValue));
        return share.getMember().getId().equals(currentUserId);
    }
}
