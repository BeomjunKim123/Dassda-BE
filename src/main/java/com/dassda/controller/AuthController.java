package com.dassda.controller;

import com.dassda.kakao.KakaoLoginParams;
import com.dassda.service.OAuthLoginService;
import com.dassda.token.AuthTokens;
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
}
