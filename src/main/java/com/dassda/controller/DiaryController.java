package com.dassda.controller;

import com.dassda.request.DiaryRequest;
import com.dassda.response.DiaryDetailResponse;
import com.dassda.response.LikesResponse;
import com.dassda.service.DiaryService;
import com.dassda.service.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
@CrossOrigin
public class DiaryController {

    private final DiaryService diaryService;
    private final LikesService likesService;

    @Operation(summary = "일기 작성 API", description = "일기 내용, 기분, 제목, 사진들")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> addDiary(
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "diaryRequest", required = false) DiaryRequest diaryRequest
    ) throws Exception {
        System.out.println(images);
        System.out.println(diaryRequest);
        diaryService.addDiary(diaryRequest, images);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "일기 상제 조회 API", description = "일기 제목, 좋아요 수, 댓글 수, 사진, 멤버 정보들")
    @GetMapping()
    public ResponseEntity<DiaryDetailResponse> getDiary(
            @RequestParam(value = "memberId") Long memberId,
            @RequestParam(value = "boardId") Long boardId,
            @RequestParam(value = "date") String date
    ) {
        DiaryDetailResponse diaryDetailRespons = diaryService.getDiaries(memberId, boardId, date);
        return ResponseEntity.ok(diaryDetailRespons);
    }
    @Operation(summary = "일기 수정 API", description = "일기 수정할 내용을 보내주셈")
    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @PathVariable(value = "diaryId") Long diaryId,
            @ModelAttribute DiaryRequest diaryRequest
            ) throws Exception {
        diaryService.updateDiary(diaryId, diaryRequest);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "일기 삭제 API", description = "일기 번호 보내주면 삭제")
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable(value = "diaryId") Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "좋아요 토글 API", description = "일기 번호 보내주면 토글 됌")
    @PostMapping("/{diaryId}/likes")
    public ResponseEntity<?> toggleLikes(@PathVariable(value = "diaryId") Long diaryId) {
        likesService.toggleLike(diaryId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "좋아요 조회 API", description = "좋아요 닉네임 사람 좋아요 수 보내기")
    @GetMapping("/{diaryId}/likes")
    public ResponseEntity<?> getLikes(@PathVariable(value = "diaryId") Long diaryId) {
        List<LikesResponse> likesResponse = likesService.getLikesForDiary(diaryId);
        return ResponseEntity.ok(likesResponse);
    }
}
