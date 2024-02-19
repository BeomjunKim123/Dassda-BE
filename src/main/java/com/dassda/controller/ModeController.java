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
@CrossOrigin
public class ModeController {

    private final ModeService modeService;

    @Operation(summary = "월별 일기 유무 API", description = "년월을 보내주면 해당하는 월에 일별로 일기 유무를 응답")
    @GetMapping("/month")
    public ResponseEntity<CalenderMonthResponse> getMonthOfExistDiary(
            @RequestParam(value = "boardId") Long boardId,
            @RequestParam(value = "date") String date
    ) {
        CalenderMonthResponse calenderMonthResponse = modeService.getMonthOfExistDiary(boardId, date);
        return ResponseEntity.ok(calenderMonthResponse);
    }

    @Operation(summary = "일별 일기 조회 API", description = "년월일을 보내주면 해당하는 날에 일기 조회(디폴트는 오늘)")
    @GetMapping("/day")
    public ResponseEntity<List<CalenderDayResponse>> getDayOfDiary(
            @RequestParam(value = "boardId") Long boardId, @RequestParam(value = "date") String date
    ) {
        List<CalenderDayResponse> calenderDayResponse = modeService.getDayOfDiary(boardId, date);
        return ResponseEntity.ok(calenderDayResponse);
    }

    @Operation(summary = "새글 유무 API", description = "일기장 아이디 보내주면 안읽은 일기가 있는지 유무")
    @GetMapping(value = "exist")
    public ResponseEntity<NewExistResponse> existNewDiary(
            @RequestParam(value = "boardId") Long boardId
    ) {
        NewExistResponse newExistResponse = modeService.existNewDiary(boardId);
        return ResponseEntity.ok(newExistResponse);
    }

    @Operation(summary = "새글 모아보기 API", description = "일기장 아이디 보내주면 읽지 않은 일기들 보내주기")
    @GetMapping(value = "new")
    public ResponseEntity<List<ModeDiaryResponse>> getReadNotDiary(
            @RequestParam(value = "boardId") Long boardId
    ) {
        List<ModeDiaryResponse> diaryList = modeService.getReadNotDiary(boardId);
        return ResponseEntity.ok(diaryList);
    }

    @Operation(summary = "전체 모아보기 API", description = "무한 스크롤 일기장 아이디, 스크롤 단위, 마지막 일기 아이디")
    @GetMapping(value = "all")
    public ResponseEntity<List<ModeDiaryResponse>> getAllDairy(
            @RequestParam(value = "boardId") Long boardId,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "lastViewId") int lastViewId
    ) {
        List<ModeDiaryResponse> diaryList = modeService.getAllDiary(boardId, pageSize, lastViewId);
        return ResponseEntity.ok(diaryList);
    }

    @Operation(summary = "전체 읽음 처리 API", description = "일기장 아이디를 보내주면 로그인한 사용자 기준 전체 읽음 처리")
    @PostMapping(value = "read")
    public ResponseEntity<Void> setReadAllStatus(@RequestParam(value = "boardId") Long boardId) {
        modeService.setReadAllStatus(boardId);
        return ResponseEntity.ok().build();
    }
}
