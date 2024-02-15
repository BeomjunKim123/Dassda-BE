package com.dassda.service;

import com.dassda.entity.Member;
import com.dassda.entity.Setting;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.SettingRepository;
import com.dassda.request.SettingRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final SettingRepository settingRepository;
    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;

    private Member member() {
        return memberRepository.findByEmail(
                SecurityContextHolder.getContext()
                        .getAuthentication().getName()
        )
                .orElseThrow(() -> new IllegalArgumentException("다시 로그인하세요"));
    }
    @Transactional
    public void saveAndNotifySetting(SettingRequest request) {
        validateRequest(request);

        Setting setting = new Setting();
        setting.setMember(member());
        setting.setQaContents(request.getQaContents());
        setting.setStarPoint(request.getStarPoint());
        setting.setRegDate(LocalDateTime.now());
        settingRepository.save(setting);

        sendEmail(request);
    }

    private void validateRequest(SettingRequest request) {
        if (request.getStarPoint() < 1 || request.getStarPoint() > 5) {
            throw new IllegalArgumentException("별점은 1에서 5 사이여야 합니다.");
        }
        if (!StringUtils.hasText(request.getQaContents())) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }

    private void sendEmail(SettingRequest requestDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("gkgk9753@gmail.com");
        message.setSubject("새로운 설정 의견");
        message.setText("의견 내용: " + requestDTO.getQaContents() + "\n별점: " + requestDTO.getStarPoint());
        mailSender.send(message);
    }

    @Transactional
    public boolean updateFontSetting(Long memberId, int font) {
        return memberRepository.findById(memberId)
                .map(member -> {
                    member.setFont(font);
                    memberRepository.save(member);
                    return true;
                }).orElse(false);
    }
}
