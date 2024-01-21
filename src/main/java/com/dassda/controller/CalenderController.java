package com.dassda.controller;

import com.dassda.response.CalenderResponse;
import com.dassda.service.CalenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calender")
public class CalenderController {

    private final CalenderService calenderService;

    @Operation(summary = "달력 1개월 일기 유무 API", description = "년월을 보내주면 해당하는 월에 일별로 일기 유무를 응답")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "sucess", description = "달력 유무 응답 성공"),
            @ApiResponse(responseCode = "fail", description = "실패함 확인좀")
    })
    @GetMapping("/month/{date}")
    public ResponseEntity<CalenderResponse> getCalender(@PathVariable String date) {

        CalenderResponse calenderResponse = calenderService.getDiaryExisting(date);

        return ResponseEntity.ok().build();
    }
    @Operation(summary = "달력 선택된 날 일기 조회 API", description = "년월일을 보내주면 해당하는 날에 일기 조회(디폴트는 오늘)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "달력 그날 조회 성공"),
            @ApiResponse(responseCode = "fail", description = "실패함 확인좀")
    })
    @GetMapping("/day/{date}")
    public ResponseEntity<Void> getDayDiary(@PathVariable String date) {



        return ResponseEntity.ok().build();
    }
}
