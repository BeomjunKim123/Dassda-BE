package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequest {

    @Schema(name = "일기장 작성 제목")
    @Size(max = 10, message = "10자를 넘을 수 없어요")
    private String title;
    @Schema(name = "일기장 디자인")
    private Integer imageNumber;
    @Schema(name = "일기장 스타일")
    private Integer appearanceType;

}
