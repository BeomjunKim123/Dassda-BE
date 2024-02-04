package com.dassda.jwt;

import com.dassda.entity.Member;
import com.dassda.repository.MemberRepository;
import com.dassda.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
@Component
@Slf4j
public class JwtTokenProvider {
    private final Key key;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, MemberRepository memberRepository, RedisService redisService) {
        this.memberRepository = memberRepository;
        this.redisService = redisService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String subject = claims.getSubject();
        Member member = new Member();
        Optional<Member> memberInfo = memberRepository.findById(Long.valueOf(subject));
        UserDetails userDetails = loadUserDetails(memberInfo.get().getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserDetails loadUserDetails(String subject) {
        Member member = memberRepository.findByEmail(subject)
                .orElseThrow(() -> new UsernameNotFoundException("유저 없다"));
        return new User(subject, "", Collections.emptyList());
    }

    //Redis에서 리프레시 토큰 존재 여부 확인
    public boolean existRefreshToken(String refreshToken) {
        return redisService.getValues(refreshToken) != null;
    }

    public long getRemainingExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            long expMillis = claims.getExpiration().getTime();
            long nowMillis = System.currentTimeMillis();
            return expMillis - nowMillis;
        } catch (JwtException e) {
            return 0; //만료되었거나 유효하지 않은 토큰인 경우
        }
    }
}
