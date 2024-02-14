package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequest {

    @Schema(name = "일기장 작성 제목")
    private String title;
    @Schema(name = "일기장 디자인")
    private Integer imageNumber;
    @Schema(name = "일기장 스타일")
    private Integer appearanceType;

}
