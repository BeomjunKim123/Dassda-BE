package com.dassda.notificationResponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class Notification {
    private Long id;
    private int notificationTypeId;
    private boolean isRead;
    private LocalDateTime regDate;
}
