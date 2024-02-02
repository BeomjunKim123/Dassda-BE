package com.dassda.response;

import com.dassda.entity.Member;
import com.dassda.entity.Sticker;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ModeDiaryResponse {

    private Long id;
    private Long memberId;
    private String nickname;
    private Long boardId;
    private Long emotionId;
    private String thumbnailUrl;
    private String title;
    private int likeCount;
    private int commentCount;
    private LocalDateTime regDate;
    private LocalDateTime selectDate;
    private String timeStamp;
}
