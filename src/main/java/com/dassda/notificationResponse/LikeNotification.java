package com.dassda.notificationResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeNotification extends DiaryNotification {
    private Long likeId;
    private String likeMemberNickname;
}
