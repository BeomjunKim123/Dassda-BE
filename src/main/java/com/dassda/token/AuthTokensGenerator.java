package com.dassda.token;

import com.dassda.jwt.JwtTokenProvider;
import com.dassda.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public AuthTokens generate(Long memberId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String subject = memberId.toString();
        String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);

        redisService.setValues(refreshToken, subject);

        return AuthTokens.of(accessToken);
    }
    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }
}
