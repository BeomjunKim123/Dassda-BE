package com.dassda.notificationResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryNotification extends Notification {
    private Long boardId;
    private String boardTitle;
    private Long diaryId;
}
