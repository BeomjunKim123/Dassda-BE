package com.dassda.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CalenderMonthResponse {
    @Schema(name = "1개월에 일기 유무")
    private List<String> dateList;
}
