package com.dassda.controller;

import com.dassda.request.CommentOrReplyRequest;
import com.dassda.response.CommentOrReplyResponse;
import com.dassda.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary/{diaryId}/comment")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "일기 댓글 쓰기 API", description = "일기 아이디를 보내면 댓글 쓰기")
    @PostMapping()
    public ResponseEntity<Void> addComment(@PathVariable(value = "diaryId") Long diaryId, @RequestBody CommentOrReplyRequest commentOrReplyRequest) {
        commentService.addComment(diaryId, commentOrReplyRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일기 댓글 수정 API", description = "일기 아이디, 댓글 아이디를 보내주면 댓글 수정 성공")
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable(value = "diaryId") Long diaryId, @PathVariable(value = "commentId") Long commentId, @RequestBody CommentOrReplyRequest commentOrReplyRequest) {
        commentService.updateComment(diaryId, commentId, commentOrReplyRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일기 댓글 조회 API", description = "일기 댓글 조회 하자 무한 스크롤 10개씩")
    @GetMapping()
    public ResponseEntity<List<CommentOrReplyResponse>> getComment(@PathVariable(value = "diaryId") Long diaryId, @RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "lastViewId") int lastViewId) {
        List<CommentOrReplyResponse> commentOrReplyResponse = commentService.getComment(diaryId, pageSize, lastViewId);
        return ResponseEntity.ok(commentOrReplyResponse);
    }

    @Operation(summary = "일기 댓글 삭제 API", description = "일기 아이디, 댓글 아이디를 보내주면 댓글 삭제 가능")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "diaryId") Long diaryId, @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(diaryId, commentId);
        return ResponseEntity.ok().build();
    }

}
