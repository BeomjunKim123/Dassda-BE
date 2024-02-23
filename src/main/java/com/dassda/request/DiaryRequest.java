package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DiaryRequest {
    @Schema(name = "일기장 아이디")
    private Long boardId;
    @Schema(description = "일기 제목")
    private String title;
    @Schema(description = "일기 내용")
    private String contents;
    @Schema(description = "sticker_id")
    private Long emotionId;
    @Schema(name = "선택된 날짜")
    private String selectedDate;

    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;

}
