package com.dassda.controller;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.repository.MemberRepository;
import com.dassda.request.ShareRequest;
import com.dassda.response.InviteInfoResponse;
import com.dassda.response.ShareResponse;
import com.dassda.service.ShareService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@CrossOrigin
public class ShareController {

    private final ShareService shareService;
    private final MemberRepository memberRepository;

    @Operation(summary = "공유 링크 생성 API", description = "일기장 정보를 링크에 담아서 응답")
    @PostMapping("/{boardId}/share")
    public ResponseEntity<Object> createShare(@PathVariable(value = "boardId") Long boardId) {
        try {
            ShareResponse response = shareService.createInvitation(boardId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            throw new IllegalStateException("존재하지 않는 해시값");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("초대 링크 생성에 실패했습니다: " + e.getMessage());
        }
    }

    @Operation(summary = "링크 정보 조회 API", description = "링크에 관련된 일기장 이름, 초대 사용자 등 정보 응답")
    @GetMapping("/share/{hash}")
    public ResponseEntity<InviteInfoResponse> accessShareByHash(@PathVariable(value = "hash") String hash) {
        try {
            InviteInfoResponse response = shareService.processSharedAccessByHash(hash);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "참여 수락 API", description = "boardId 보내주면 share 테이블에 삽입")
    @PostMapping("/{boardId}/join")
    public ResponseEntity<Void> joinDiary(
            @PathVariable(value = "boardId") Long boardId
    ) {
        shareService.joinDiary(boardId);
        return ResponseEntity.ok().build();
    }
}
