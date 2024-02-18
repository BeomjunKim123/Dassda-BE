package com.dassda.controller;

import com.dassda.request.FontSettingRequest;
import com.dassda.request.SettingRequest;
import com.dassda.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setting")
@CrossOrigin
public class SettingController {

    private final SettingService settingService;

    @PostMapping
    public ResponseEntity<String> submitSetting(@RequestBody SettingRequest request) {
        settingService.saveAndNotifySetting(request);
        return ResponseEntity.ok("의견이 성공적으로 전송되었습니다.");
    }

    @PutMapping
    public ResponseEntity<?> updateFontSetting(@RequestBody FontSettingRequest fontSettingRequest) {
        boolean isUpdated = settingService.updateFontSetting(
                fontSettingRequest.getMemberId(),
                fontSettingRequest.getFont()
        );

        if (isUpdated) {
            return ResponseEntity.ok("폰트 설정이 업데이트 되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("해당 사용자를 찾을 수 없습니다.");
        }
    }
}
