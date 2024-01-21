package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "달력 요청 데이터")
public class CalenderRequest {
    private String date;
}
