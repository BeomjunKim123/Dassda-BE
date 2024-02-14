package com.dassda.notificationResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyNotification extends CommentNotification {
    private Long replyId;
    private String replyContent;
    private String replyWriterNickname;
}
