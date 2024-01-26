package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Schema(description = "일기 조회 요청 데이터")
public class DiaryRequest {
    private Long id;
    private Long boardId;
    private String diaryTitle;
    private String contents;
    private Long stickerId;
    private List<MultipartFile> diaryImgs;

}
