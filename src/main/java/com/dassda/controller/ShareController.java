package com.dassda.controller;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.repository.MemberRepository;
import com.dassda.request.ShareRequest;
import com.dassda.response.ShareResponse;
import com.dassda.service.ShareService;
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
public class ShareController {

    private final ShareService shareService;
    private final MemberRepository memberRepository;

    @PostMapping("/{boardId}/share")
    public ResponseEntity<Object> createShare(@PathVariable(value = "boardId") Long boardId, @RequestBody ShareRequest request) {
        try {
            ShareResponse response = shareService.createInvitation(
                    boardId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("초대 링크 생성에 실패했습니다." + e.getMessage());
        }
    }

//    @GetMapping("/{boardId}/share")
//    public ResponseEntity<?> accessShare(@PathVariable(value = "boardId") Long boardId, @RequestParam(value = "hash") String hash) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return shareService.processSharedAccess(boardId, hash, authentication);
//    }
}
