package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class DiaryRequest {
    @Schema(description = "diary_id")
    private Long id;
    @Schema
    private Long boardId;
    @Schema(description = "일기 제목")
    private String diaryTitle;
    @Schema(description = "일기 내용")
    private String contents;
    @Schema(description = "sticker_id")
    private Long stickerId;
    @Schema(description = "멀티파트 이미지 배열")
    private List<MultipartFile> diaryImgs;

}
