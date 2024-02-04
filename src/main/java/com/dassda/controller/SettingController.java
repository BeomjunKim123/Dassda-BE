package com.dassda.controller;

import com.dassda.repository.MemberRepository;
import com.dassda.request.SettingRequest;
import com.dassda.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setting")
public class SettingController {

    private final SettingService settingService;
    private final MemberRepository memberRepository;


    //폰트 설정을 업데이트하는 메소드
    @PutMapping
    public ResponseEntity<?> updateFontSetting(@RequestBody SettingRequest SettingRequest,
                                               @AuthenticationPrincipal UserDetails currentUser) {
        //현재 인증된 사용자의 username을 가져옴
        String email = currentUser.getUsername();
        //이메일을 사용하여 사용자의 실제 ID를 데이터베이스에서 조회
        Long userId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        //userId를 사용하여 사용자의 폰트 설정
        boolean isUpdated = settingService.updateFontSetting(SettingRequest.getUserId(), SettingRequest.getFontSize(), SettingRequest.getFontType());

        if (isUpdated) {
            return ResponseEntity.ok().body("폰트 설정이 업데이트 되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("폰트 설정 업데이트에 실패했습니다.");
        }
    }
}
