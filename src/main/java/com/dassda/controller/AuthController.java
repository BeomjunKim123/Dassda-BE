package com.dassda.controller;

import com.dassda.kakao.KakaoLoginParams;
import com.dassda.service.OAuthLoginService;
import com.dassda.token.AuthTokens;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final OAuthLoginService oAuthLoginService;
    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        System.out.println(params);
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }
//    @PostMapping("/kakao")
//    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params, HttpServletResponse response) {
//        // OAuthLoginService의 login 메소드를 호출할 때 HttpServletResponse 객체도 함께 전달합니다.
//        AuthTokens tokens = oAuthLoginService.login(params, response); // 수정된 login 메소드 호출
//        return ResponseEntity.ok(tokens); // 클라이언트에게 JWT 토큰을 쿠키로 설정한 후, 응답을 반환합니다.
//    }
}
