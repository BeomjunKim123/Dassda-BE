package com.dassda.controller;

import com.dassda.notificationResponse.Notification;
import com.dassda.request.NotificationRequest;
import com.dassda.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 전체 조회 API", description = "모든 알림 응답")
    @GetMapping()
    public ResponseEntity<List<Notification>> getNotification(
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "lastViewId") int lastViewId
    ) throws JsonProcessingException {
        List<Notification> notifications = notificationService.getUserNotifications(pageSize, lastViewId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "알림 id에 대한 읽음 처리 API", description = "하나의 알림 읽음 처리")
    @PutMapping("/{notificationId}")
    public ResponseEntity<Void> updateReadStatusOfOne(
            @PathVariable(value = "notificationId") Long notificationId,
            @RequestBody() NotificationRequest notificationRequest
            ) throws JsonProcessingException {
        notificationService.updateReadStatusOfOne(notificationId, notificationRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전체 알림 읽음 처리 API", description = "전체 알림 읽음 처리")
    @PostMapping()
    public ResponseEntity<Void> updateReadStatusAll() throws JsonProcessingException {
        notificationService.updateReadStatusAll();
        return ResponseEntity.ok().build();
    }
}
