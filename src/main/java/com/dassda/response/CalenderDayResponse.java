package com.dassda.response;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Sticker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class CalenderDayResponse {
    @Schema(name = "일기")
    private Long id;
    @Schema(name = "사용자 아이디")
    private Long memberId;
    @Schema(name = "닉네임")
    private String nickname;
    @Schema(name = "일기장 아이디")
    private Long boardId;
    @Schema(name = "기분 아이디")
    private Long emotionId;
    @Schema(name = "일기 섬네일 사진 한장")
    private String thumbnailUrl;
    @Schema(name = "일기 제목")
    private String title;
    @Schema(name = "좋아요 수")
    private int likeCount;
    @Schema(name = "댓글 수")
    private int commentCount;
    @Schema(name = "선택된 날짜")
    private LocalDate selectedDate;
    @Schema(name = "작성일부터 과거로 일수")
    private String timeStamp;

}
