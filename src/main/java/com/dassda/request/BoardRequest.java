package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "일기장 요청 데이터")
public class BoardRequest {
    private Long id;
    private String boardTitle;
    private int imageNumber;
    private int appearanceType;
}
