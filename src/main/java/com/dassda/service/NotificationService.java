package com.dassda.service;

import com.dassda.entity.Member;
import com.dassda.notificationResponse.*;
import com.dassda.repository.MemberRepository;
import com.dassda.utils.GetMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private Member member() {
        return GetMember.getCurrentMember();
    }
    public boolean existsNotification() {
        String key = "notification:" + member().getId() + ":*";
        Set<String> keys = redisTemplate.keys(key);
        return keys != null && !keys.isEmpty();
    }
    public List<Notification> getUserNotifications(int pageSize, int lastViewId) throws JsonProcessingException{
        String pattern = "notification:*" + member().getId() + ":*";
        List<Notification> notifications = new ArrayList<>();

        Set<String> keys = redisTemplate.keys(pattern);
        if(keys != null) {
            List<Long> sortedIds = keys.stream()
                    .map(this::extractId)
                    .sorted()
                    .collect(Collectors.toList());

            int startIndex = lastViewId == 0 ? 0 : sortedIds.indexOf(lastViewId) + 1;
            List<Long> pageIds = sortedIds.subList(startIndex, Math.min(startIndex + pageSize, sortedIds.size()));

            for(Long id : pageIds) {
                String key = "notification:" + member().getId() + ":" + id;
                String notificationJson = redisTemplate.opsForValue().get(key);

                if(notificationJson != null) {
                    Notification notification = parseNotification(notificationJson);
                    JsonNode root = objectMapper.readTree(notificationJson);
                    boolean checkRead = root.get("isRead").asBoolean();
                    if(checkRead) {
                        notification.setRead(true);
                    }
                    notification.setId(id);
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
        JsonNode root = objectMapper.readTree(json);
        if(!root.has("notificationTypeId")) {
            throw new JsonMappingException("알림 타입 에러");
        }
        int typeId = root.get("notificationTypeId").asInt();
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
    public void updateReadStatusOfOne(Long notificationId) throws JsonProcessingException {
        String key = "notification:" + member().getId() + ":" + notificationId;

        String notificationJson = redisTemplate.opsForValue().get(key);
        if(notificationJson != null) {
            JsonNode root = objectMapper.readTree(notificationJson);
            ((ObjectNode) root).put("isRead", true);
            String updateJson = objectMapper.writeValueAsString(root);
            redisTemplate.opsForValue().set(key, updateJson);
        } else {
            throw new IllegalStateException("알림 데이터를 찾을 수 없음.");
        }
    }
    public void updateReadStatusAll() throws JsonProcessingException {
        String keyPattern = "notification:" + member().getId() + ":*";
        Set<String> keys = redisTemplate.keys(keyPattern);

        if(keys != null) {
            for(String key : keys) {
                String notificationJson = redisTemplate.opsForValue().get(key);
                if(notificationJson != null) {
                    JsonNode root = objectMapper.readTree(notificationJson);
                    ((ObjectNode) root).put("isRead", true);
                    String updateJson = objectMapper.writeValueAsString(root);
                    redisTemplate.opsForValue().set(key, updateJson);
                } else {
                    throw new IllegalStateException("알림 데이터가 없다.");
                }
            }
        }
    }
}
