package com.dassda.notificationResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentNotification extends DiaryNotification {
    private Long commentId;
    private String commentContent;
    private String commentWriterNickname;
}
