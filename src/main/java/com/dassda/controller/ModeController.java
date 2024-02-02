package com.dassda.controller;

import com.dassda.response.CalenderDayResponse;
import com.dassda.response.CalenderMonthResponse;
import com.dassda.response.ModeDiaryResponse;
import com.dassda.response.NewExistResponse;
import com.dassda.service.ModeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mode")
@CrossOrigin("http://localhost:3000/**")
public class ModeController {

    private final ModeService modeService;

    @Operation(summary = "월별 일기 유무 API", description = "년월을 보내주면 해당하는 월에 일별로 일기 유무를 응답")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "sucess", description = "달력 유무 응답 성공"),
            @ApiResponse(responseCode = "fail", description = "실패함 확인좀")
    })
    @GetMapping("/month")
    public ResponseEntity<CalenderMonthResponse> getMonthOfExistDiary(@RequestParam Long boardId, @RequestParam String date) {
        System.out.println(date);
        CalenderMonthResponse calenderMonthResponse = modeService.getMonthOfExistDiary(boardId, date);
        return ResponseEntity.ok(calenderMonthResponse);
    }


    @Operation(summary = "일별 일기 조회 API", description = "년월일을 보내주면 해당하는 날에 일기 조회(디폴트는 오늘)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "달력 그날 조회 성공"),
            @ApiResponse(responseCode = "fail", description = "실패함 확인좀")
    })
    @GetMapping("/day")
    public ResponseEntity<List<CalenderDayResponse>> getDayOfDiary(@RequestParam Long boardId, @RequestParam String date) {
        List<CalenderDayResponse> calenderDayResponse = modeService.getDayOfDiary(boardId, date);
        return ResponseEntity.ok(calenderDayResponse);
    }

    @Operation(summary = "새글 유무 API", description = "일기장 아이디 보내주면 안읽은 일기가 있는지 유무")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "새글 유무 응답 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @GetMapping(value = "exist")
    public ResponseEntity<NewExistResponse> existNewDiary(@RequestParam Long boardId) {
        NewExistResponse newExistResponse = modeService.existNewDiary(boardId);
        return ResponseEntity.ok(newExistResponse);
    }

    @Operation(summary = "새글 모아보기 API", description = "일기장 아이디 보내주면 읽지 않은 일기들 보내주기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "새글 모아보기 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @GetMapping(value = "new")
    public ResponseEntity<List<ModeDiaryResponse>> getReadNotDiary(@RequestParam Long boardId) {
        List<ModeDiaryResponse> diaryList = modeService.getReadNotDiary(boardId);
        return ResponseEntity.ok(diaryList);
    }


    @Operation(summary = "전체 모아보기 API", description = "무한 스크롤 일기장 아이디, 스크롤 단위, 마지막 일기 아이디")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "모아보기 응답 성공"),
            @ApiResponse(responseCode = "fila", description = "실패")
    })
    @GetMapping(value = "all")
    public ResponseEntity<List<ModeDiaryResponse>> getAllDairy(@RequestParam Long boardId, @RequestParam int pageSize, @RequestParam int lastViewId) {
        List<ModeDiaryResponse> diaryList = modeService.getAllDiary(boardId, pageSize, lastViewId);
        return ResponseEntity.ok(diaryList);
    }


    @Operation(summary = "전체 읽음 처리 API", description = "일기장 아이디를 보내주면 로그인한 사용자 기준 전체 읽음 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "읽음 처리 완료"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @PostMapping(value = "read")
    public ResponseEntity<Void> setReadAllStatus(@RequestParam Long boardId) {
        modeService.setReadAllStatus(boardId);
        return ResponseEntity.ok().build();
    }
}
