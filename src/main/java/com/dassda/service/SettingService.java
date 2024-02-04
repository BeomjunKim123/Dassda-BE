package com.dassda.service;

import com.dassda.entity.Setting;
import com.dassda.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    // 데이터베이스에서 사용자의 폰트 설정을 찾아 업데이트하는 메소드
    public boolean updateFontSetting(Long userId, String fontSize, String fontType) {
        Setting setting = settingRepository.findByUserId(userId);
        if (setting != null) {
            setting.setFontSize(fontSize);
            setting.setFontType(fontType);
            settingRepository.save(setting);
            return true;
        } else {
            // 존재하지 않는 사용자 ID의 경우에는 적절한 예외 처리나 비즈니스 로직을 구현해야 합니다.
            return false;
        }
    }
}
