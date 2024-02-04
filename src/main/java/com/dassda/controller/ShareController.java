package com.dassda.controller;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.repository.MemberRepository;
import com.dassda.request.InvitationRequest;
import com.dassda.service.ShareService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class ShareController {

    private final ShareService shareService;
    private final MemberRepository memberRepository;

    //일기장 공유 생성 요청 처리
    @PostMapping("/{boardId}/share")
    public ResponseEntity<?> createShareInvitation(@PathVariable Long boardId, @RequestBody InvitationRequest request) {
        try {
            Share share = shareService.createInvitation(
                    boardId, request.getMemberId(), request.getName(), request.getProfileImageUrl(), request.getTitle());
            String invitationLink = "http://localhost:8080/invite?hash=" + share.getHashValue();
            System.out.println(boardId);
            return ResponseEntity.ok().body("초대 링크가 생성되었습니다. 링크: " + invitationLink);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("초대 링크 생성에 실패했습니다. " + e.getMessage());
        }
    }

    @GetMapping("/{boardId}/share")
    public ResponseEntity<?> accessSharedBoard(@PathVariable Long boardId, @RequestParam String hashValue) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            // currentUsername을 사용하여 데이터베이스에서 현재 사용자의 ID를 조회
            Member currentMember = memberRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with username: " + currentUsername));
            Long currentUserId = currentMember.getId();

            try {
                Board board = shareService.accessInvitation(hashValue);
                if (board.getId().equals(boardId) && shareService.isInvitedUser(currentUserId, hashValue)) {
                    return ResponseEntity.ok(board);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
                }
            } catch (EntityNotFoundException e) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다. " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증되지 않은 사용자입니다.");
        }
    }
}
