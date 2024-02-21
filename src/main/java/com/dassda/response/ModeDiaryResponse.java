package com.dassda.response;

import com.dassda.entity.Member;
import com.dassda.entity.Sticker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ModeDiaryResponse {
    @Schema(name = "일기")
    private Long id;
    @Schema(name = "")
    private Long memberId;
    @Schema(name = "")
    private String nickname;
    @Schema(name = "")
    private Long boardId;
    @Schema(name = "")
    private Long emotionId;
    @Schema(name = "")
    private String thumbnailUrl;
    @Schema(name = "")
    private String title;
    @Schema(name = "")
    private int likeCount;
    @Schema(name = "")
    private int commentCount;
    @Schema(name = "")
    private LocalDateTime regDate;
    @Schema(name = "")
    private LocalDateTime selectedDate;
    @Schema(name = "")
    private String timeStamp;
}
