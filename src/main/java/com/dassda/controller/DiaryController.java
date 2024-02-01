package com.dassda.controller;

import com.dassda.request.DiaryRequest;
import com.dassda.response.DiaryDetailResponse;
import com.dassda.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
@CrossOrigin("http://localhost:3000/**")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "일기 작성 API", description = "일기 내용, 기분, 제목, 사진들")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기 작성 성공"),
            @ApiResponse(responseCode = "fail", description = "작성 실패 오류 찾으셈")
    })
    @PostMapping()
    public ResponseEntity<Void> addDiary(@ModelAttribute DiaryRequest diaryRequest) throws Exception {
        diaryService.addDiary(diaryRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "일기 상제 조회 API", description = "일기 제목, 좋아요 수, 댓글 수, 사진, 멤버 정보들")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기 조회 성공"),
            @ApiResponse(responseCode = "fail", description = "조회 실패 오류 찾으셈")
    })
    @GetMapping()
    public ResponseEntity<List<DiaryDetailResponse>> getDiary(@RequestParam Long boardId, @RequestParam String date) {
        List<DiaryDetailResponse> diaryDetailRespons = diaryService.getDiaries(boardId, date);
        return ResponseEntity.ok(diaryDetailRespons);
    }


    @Operation(summary = "일기 수정 API", description = "일기 수정할 내용을 보내주셈")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기 수정 성공"),
            @ApiResponse(responseCode = "fail", description = "수정 실패 오류 찾으셈")
    })
    @PutMapping()
    public ResponseEntity<Void> updateDiary(@RequestBody DiaryRequest diaryRequest) throws Exception {
        diaryService.updateDiary(diaryRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "일기 삭제 API", description = "일기 번호 보내주면 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기 삭제 성공"),
            @ApiResponse(responseCode = "fail", description = "삭제 실패 오류 찾으셈")
    })
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "좋아요 토글 API", description = "일기 번호 보내주면 토글 됌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "좋아요 성공"),
            @ApiResponse(responseCode = "fail", description = "좋아요 실패")
    })
    @PostMapping("/{diaryId}/likes")
    public ResponseEntity<?> toggleLikes(@PathVariable Long diaryId) {
        return null;
    }


    @Operation(summary = "좋아요 조회 API", description = "좋아요 닉네임 사람 좋아요 수 보내기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "좋아요 조회 성공"),
            @ApiResponse(responseCode = "fail", description = "좋아요 조회 실패")
    })
    @GetMapping("/{diaryId}/likes")
    public ResponseEntity<?> getLikes(@PathVariable Long diaryId) {
        return null;
    }
}
