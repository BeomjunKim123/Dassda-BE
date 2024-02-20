package com.dassda.controller;

import com.dassda.entity.Member;
import com.dassda.jwt.JwtTokenProvider;
import com.dassda.repository.MemberRepository;
import com.dassda.request.MembersRequest;
import com.dassda.service.MemberService;
import com.dassda.service.RedisService;
import com.dassda.token.AuthTokens;
import com.dassda.token.AuthTokensGenerator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@CrossOrigin
public class MemberController {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Operation(summary = "액세스 토큰에 해당하는 멤버 API", description = "토큰을 보내주면 토근에 저장되어 있는 멤버의 정보를 응답한다.")
    @GetMapping("/{accessToken}")
    public ResponseEntity<Member> findByAccessToken(@PathVariable String accessToken) {
        Long memberId = authTokensGenerator.extractMemberId(accessToken);
        return ResponseEntity.ok(memberRepository.findById(memberId).get());
    }

    @Operation(summary = "로그아웃 API", description = "헤더에 담긴 액세스 토큰의 만료 기간을 마감시킨다.")
    @PostMapping(value = "logout")
    public ResponseEntity<Object> findByLogout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Acceess Token");
        }
        Long memberId;
        try {
            String subject = jwtTokenProvider.extractSubject(accessToken);
            memberId = Long.valueOf(subject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Access Token");
        }
        redisService.delValues(accessToken);
        long remainingTime = jwtTokenProvider.getRemainingExpiration(accessToken);
        if (remainingTime > 0) {
            redisService.addToBlacklist(accessToken, Duration.ofMillis(remainingTime));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리프레시 토큰 API", description = "만료된 액세스을 요청하면 리프레시 토큰을 조회하고 새로운 액세스 토큰을 응답한다.")
    @PatchMapping()
    public ResponseEntity<AuthTokens> refreshToken(HttpServletRequest request) {
        AuthTokens authTokens = redisService.getValues(request);
        return ResponseEntity.ok(authTokens);
    }

    @Operation(summary = "프로필 정보 수정 API", description = "사진, 닉네임 둘 중 하나를 변경도 가능하다.")
    @PostMapping("/update")
    public ResponseEntity<Void> updateProfile(@ModelAttribute MembersRequest membersRequest) throws Exception {
        memberService.updateProfile(membersRequest);
        return ResponseEntity.ok().build();
    }
}
