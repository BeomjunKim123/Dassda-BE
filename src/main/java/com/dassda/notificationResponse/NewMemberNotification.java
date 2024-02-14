package com.dassda.notificationResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMemberNotification extends DiaryNotification {
    private String newMemberNickname;
}
