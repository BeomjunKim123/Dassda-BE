package com.dassda.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "달력 한달 일기 유무 응답 데이터")
public class CalenderMonthResponse {
    List<String> diaryDate;
}
