package com.dassda.controller;

import com.dassda.entity.Member;
import com.dassda.oauth.RequestOAuthInfoService;
import com.dassda.repository.MemberRepository;
import com.dassda.service.OAuthLoginService;
import com.dassda.token.AuthTokensGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<Member>> findAll() {
        return ResponseEntity.ok(memberRepository.findAll());
    }
    @GetMapping("/{accessToken}")
    public ResponseEntity<Member> findByAccessToken(@PathVariable String accessToken) {
        Long memberId = authTokensGenerator.extractMemberId(accessToken);
        return ResponseEntity.ok(memberRepository.findById(memberId).get());
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
