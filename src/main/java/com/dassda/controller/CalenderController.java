package com.dassda.controller;

import com.dassda.response.CalenderDayResponse;
import com.dassda.response.CalenderMonthResponse;
import com.dassda.service.CalenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calender")
@CrossOrigin("http://localhost:3000/**")
public class CalenderController {

    private final CalenderService calenderService;

    @Operation(summary = "달력 1개월 일기 유무 API", description = "년월을 보내주면 해당하는 월에 일별로 일기 유무를 응답")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "sucess", description = "달력 유무 응답 성공"),
            @ApiResponse(responseCode = "fail", description = "실패함 확인좀")
    })
    @GetMapping("/month")
    public ResponseEntity<CalenderMonthResponse> getCalender(@RequestParam String date) {
        System.out.println(date);
        CalenderMonthResponse calenderMonthResponse = calenderService.getMonth(date);
        return ResponseEntity.ok(calenderMonthResponse);
    }
    @Operation(summary = "달력 선택된 날 일기 조회 API", description = "년월일을 보내주면 해당하는 날에 일기 조회(디폴트는 오늘)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "달력 그날 조회 성공"),
            @ApiResponse(responseCode = "fail", description = "실패함 확인좀")
    })
    @GetMapping("/day")
    public ResponseEntity<List<CalenderDayResponse>> getDayDiary(@RequestParam Long boardId, @RequestParam String date) {
        List<CalenderDayResponse> calenderDayResponse = calenderService.getDiaries(boardId, date);
        return ResponseEntity.ok(calenderDayResponse);
    }
}
