package com.dassda.controller;

import com.dassda.request.CommentOrReplyRequest;
import com.dassda.response.CommentOrReplyResponse;
import com.dassda.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/{commentId}/reply")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "답글 작성 API", description = "다이어리 아이디, 댓글 아이디 보내주면 작성 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "답글 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @PostMapping()
    public ResponseEntity<CommentOrReplyRequest> addReply(
            @PathVariable(value = "commentId") Long commentId,
            @RequestBody CommentOrReplyRequest commentOrReplyRequest
    ) {
        replyService.addReply(commentId, commentOrReplyRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "답글 수정 API", description = "다이어리 아이디, 댓글 아이디, 답글 아이디 보내주면 수정 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "답글 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @PutMapping("/{replyId}")
    public ResponseEntity<Void> updateReply(
            @PathVariable(value = "commentId") Long commentId,
            @PathVariable(value = "replyId") Long replyId,
            @RequestBody CommentOrReplyRequest commentOrReplyRequest
    ) {
        replyService.updateReply(commentId, replyId, commentOrReplyRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "답글 삭제 API", description = "다이어리 아이디, 댓글 아이디, 답글 아이디 보내주면 삭제 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "답글 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable(value = "commentId") Long commentId,
            @PathVariable(value = "replyId") Long replyId
    ) {
        replyService.deleteReply(commentId, replyId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "답글 조회 API", description = "다이어리 아이디, 댓글 아이디 보내주면 조회 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "답글 성공"),
            @ApiResponse(responseCode = "fail", description = "실패")
    })
    @GetMapping()
    public ResponseEntity<List<CommentOrReplyResponse>> getReply(
            @PathVariable(value = "commentId") Long commentId,
            @RequestParam(value = "lastViewId") int lastViewId
    ) {
        List<CommentOrReplyResponse> commentOrReplyResponseList = replyService.getReply(commentId, lastViewId);
        return ResponseEntity.ok(commentOrReplyResponseList);
    }
}
