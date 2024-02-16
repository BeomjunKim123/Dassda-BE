package com.dassda.service;

import com.dassda.entity.Member;
import com.dassda.jwt.JwtTokenProvider;
import com.dassda.token.AuthTokens;
import com.dassda.utils.GetMember;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class RedisService {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private Member member() {
        return GetMember.getCurrentMember();
    }
    public void delValues(String token) {
        redisTemplate.delete(token.substring(7));
    }

    public void addToBlacklist(String token, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, "blacklisted", duration);
    }

    public boolean haskeyBlackList(String token) {
        return redisTemplate.hasKey(token);
    }

    public void setValues(String refreshToken, String memberId) {
        String key = "refreshToken:" + memberId;
        redisTemplate.opsForValue().set(key, refreshToken, 30, TimeUnit.DAYS);
    }

    public AuthTokens getValues(HttpServletRequest request) {
        String accessToken = resolveToken(request);
        System.out.println(jwtTokenProvider.getRemainingTime(accessToken));
        String refreshTokenKey = "refreshToken:" + member().getId();
        String refreshToken = redisTemplate.opsForValue().get(refreshTokenKey);
        if (refreshToken != null && jwtTokenProvider.validateToken(accessToken)) {
            Long memberId = Long.valueOf(jwtTokenProvider.extractSubject(refreshToken));

            long now = (new Date()).getTime();
            Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // 액세스 토큰 만료 시간 설정
            String newAccessToken = jwtTokenProvider.generate(memberId.toString(), accessTokenExpiredAt);
            System.out.println(jwtTokenProvider.getRemainingTime(newAccessToken));
            return new AuthTokens(newAccessToken);
        } else {
            throw new IllegalStateException("유효한 리프레시 토큰이 없다");
        }

    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
