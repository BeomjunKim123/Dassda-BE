package com.dassda.service;

import com.dassda.entity.Member;
import com.dassda.notificationResponse.*;
import com.dassda.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final StringRedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    private Member member() {
        return memberRepository.findByEmail(
                SecurityContextHolder.getContext()
                        .getAuthentication().getName()
        )
                .orElseThrow(() -> new IllegalStateException("없다"));
    }
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications() throws JsonProcessingException{
        String pattern = "notification:" + member().getId() + ":*";
        List<Notification> notifications = new ArrayList<>();

        Set<String> keys = redisTemplate.keys(pattern);
        if(keys != null) {
            for (String key : keys) {
                String notificationJson = redisTemplate.opsForValue().get(key);
                if(notificationJson != null) {
                    Notification notification = parseNotification(notificationJson);
                    Long notificationId = extractId(key);
                    notification.setId(notificationId);
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }

    private Long extractId(String key) {
        String[] parts = key.split(":");
        return Long.parseLong(parts[parts.length - 1]);
    }
    private Notification parseNotification(String json) throws JsonProcessingException {
        System.out.println(json);
        JsonNode root = objectMapper.readTree(json);

        if(!root.has("notificationTypeId")) {
            throw new JsonMappingException("알림 타입 에러");
        }
        int typeId = root.get("notificationTypeId").asInt();
//        Long id = redisTemplate.opsForValue().get();

        switch (typeId) {
            case 1:
                return objectMapper.treeToValue(root, CommentNotification.class);
            case 2:
                return objectMapper.treeToValue(root, ReplyNotification.class);
            case 3:
                return objectMapper.treeToValue(root, LikeNotification.class);
            case 4:
                return objectMapper.treeToValue(root, DiaryNotification.class);
            case 5:
                return objectMapper.treeToValue(root, NewMemberNotification.class);
            default:
                throw new IllegalArgumentException("지원하지 않는 알림 타입: " + typeId);
        }
    }
}
