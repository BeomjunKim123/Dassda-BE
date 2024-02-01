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
    private Member member;
    private Long boardId;
    private Sticker emotionId;
    private String thumbnailUrl;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime regDate;
    private LocalDateTime selectDate;
}
