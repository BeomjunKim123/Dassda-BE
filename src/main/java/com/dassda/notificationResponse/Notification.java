package com.dassda.notificationResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Data
public class Notification {
    private Long id;
    private int notificationTypeId;
    @JsonProperty("isRead")
    private boolean isRead;
    private LocalDateTime regDate;
}
