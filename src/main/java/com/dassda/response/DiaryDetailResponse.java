package com.dassda.response;

import com.dassda.entity.DiaryImg;
import com.dassda.request.DiaryImgRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class DiaryDetailResponse {
    @Schema
    private Long id;
    @Schema
    private Long memberId;
    @Schema
    private String nickname;
    @Schema
    private String profileUrl;
    @Schema
    private Long emotionId;
    @Schema(ref = "#/components/schemas/DiaryImgRequest")
    private List<DiaryImgRequest> images;
    @Schema
    private String title;
    @Schema
    private String contents;
    @Schema(name = "작성일부터 과거로 일수")
    private String timeStamp;
    @Schema
    private LocalDateTime selectedDate;
    @Schema
    private int likeCount;
    @Schema
    private int commentCount;
    @Schema
    private boolean isOwned;

}
