package com.dassda.controller;

import com.dassda.request.CommentRequest;
import com.dassda.response.CommentResponse;
import com.dassda.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary/{diaryId}/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "일기 댓글 쓰기 API", description = "일기 아이디를 보내면 댓글 쓰기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기 아이디 보내"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @PostMapping()
    public ResponseEntity<Void> addComment(@PathVariable Long diaryId, @RequestBody CommentRequest commentRequest) {
        commentService.addComment(diaryId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일기 댓글 수정 API", description = "일기 아이디, 댓글 아이디를 보내주면 댓글 수정 성공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long diaryId, @PathVariable Long commentId) {
        commentService.updateComment(diaryId, commentId);
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "일기 댓글 조회 API", description = "일기 댓글 조회 하자 무한 스크롤 10개씩")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기 댓글 조회 성공"),
            @ApiResponse(responseCode = "fail", description = "실패함")
    })
    @GetMapping()
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long diaryId, @RequestParam int pageSize, @RequestParam int lastViewId) {
        CommentResponse commentResponse = commentService.getComment(diaryId, pageSize, lastViewId);
        return ResponseEntity.ok(commentResponse);
    }



    @Operation(summary = "일기 댓글 삭제 API", description = "일기 아이디, 댓글 아이디를 보내주면 댓글 삭제 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long diaryId, @PathVariable Long commentId) {
        commentService.deleteComment(diaryId, commentId);
        return ResponseEntity.ok().build();
    }

}
