package com.dassda.controller;

import com.dassda.entity.Member;
import com.dassda.jwt.JwtTokenProvider;
import com.dassda.oauth.RequestOAuthInfoService;
import com.dassda.repository.MemberRepository;
import com.dassda.service.OAuthLoginService;
import com.dassda.service.RedisService;
import com.dassda.token.AuthTokensGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.tuple.CreationTimestampGeneration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final OAuthLoginService oAuthLoginService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<List<Member>> findAll() {
        return ResponseEntity.ok(memberRepository.findAll());
    }
    @GetMapping("/{accessToken}")
    public ResponseEntity<Member> findByAccessToken(@PathVariable String accessToken) {
        Long memberId = authTokensGenerator.extractMemberId(accessToken);
        return ResponseEntity.ok(memberRepository.findById(memberId).get());
    }

//    @PostMapping(value = "logout")
//    public ResponseEntity<Object> findByLogout(HttpServletRequest request) {
//        String accessToken = request.getHeader("Authorization");
//        oAuthLoginService.logout(accessToken);
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", "로그아웃 성공");
//        return ResponseEntity.ok(response);
//    }

    @PostMapping(value = "logout")
    public ResponseEntity<Object> findByLogout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");

        //Access Token검증
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Acceess Token");
        }

        //Access Token에서 회원 ID추출 및 검증
        Long memberId;
        try {
            String subject = jwtTokenProvider.extractSubject(accessToken);
            memberId = Long.valueOf(subject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Access Token");
        }

        //Redis에서 Refresh Token 삭제
        redisService.delValues(accessToken);

        //Access Token을 블랙리스트에 추가
        long remainingTime = jwtTokenProvider.getRemainingExpiration(accessToken);
        if (remainingTime > 0) {
            redisService.addToBlacklist(accessToken, Duration.ofMillis(remainingTime));
        }

        //로그아웃 성공 응답
        Map<String, Object> response = new HashMap<>();
        response.put("success", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateMembers() {
        return null;
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMembers() {
        return null;
    }
}
