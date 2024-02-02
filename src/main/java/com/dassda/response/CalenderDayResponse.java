package com.dassda.response;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Sticker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class CalenderDayResponse {

    private Long id;
    private Long memberId;
    private String nickname;
    private Long boardId;
    private Long emotionId;
    private String thumbnailUrl;
    private String title;
    private int likeCount;
    private int commentCount;
    private LocalDateTime selectedDate;
    private String timeStamp;

}
