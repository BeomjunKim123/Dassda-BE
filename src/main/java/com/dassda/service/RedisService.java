package com.dassda.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    //키-벨류 설정
    public void setValues(String token, String email, Duration expireDuration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, email, Duration.ofDays(30)); //리프레시 토큰이 사용될때마다 만료시간을 30일로 재설정
    }

    //키값으로 벨류 가져오기
    public String getValues(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    //키-벨류 삭제
    public void delValues(String token) {
        redisTemplate.delete(token.substring(7));
    }

    //AccessToken 블랙리스트에 등록
    public void addToBlacklist(String token, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, "blacklisted", duration);
    }

    public boolean haskeyBlackList(String token) {
        return redisTemplate.hasKey(token);
    }
}
