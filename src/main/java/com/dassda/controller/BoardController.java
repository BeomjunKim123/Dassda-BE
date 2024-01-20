package com.dassda.controller;

import com.dassda.entity.Board;
import com.dassda.jwt.JwtTokenProvider;
import com.dassda.request.BoardRequest;
import com.dassda.response.BoardResponse;
import com.dassda.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.ObjectEqualityComparator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "일기장 추가 API", description = "일기장 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기장 추가 완료"),
            @ApiResponse(responseCode = "fail", description = "일기장 추가 실패, 예외 처리 확인")
    })
    @PostMapping("/")
    public ResponseEntity<Object> addBoard(@RequestBody BoardRequest boardRequest) {
        boardService.addBoard(boardRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("success", "일기장 추가 성공");
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "일기장 삭제 API", description = "일기장 아이디 값으로 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기장 삭제 완료"),
            @ApiResponse(responseCode = "fail", description = "일기장 삭제 실패, 예외 처리 확인")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", "일기장 삭제 성공");
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "일기장 조회 API", description = "일기 개수, 3일 이내의 일기 존재 유무, 공유 일기장 유무 추가적인 데이터")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기장 조회 성공"),
            @ApiResponse(responseCode = "fail", description = "일기장 조회 실패")
    })
    @GetMapping("/")
    public ResponseEntity<List<BoardResponse>> getBoard() {
        List<BoardResponse> boards = boardService.getBoard();
        return ResponseEntity.ok(boards);
    }
    @Operation(summary = "일기장 편집 API", description = "일기장 편집 PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "일기장 편집 성공"),
            @ApiResponse(responseCode = "fail", description = "일기장 편집 실패")
    })
    @PutMapping("/")
    public ResponseEntity<Object> updateBoard(@RequestBody BoardRequest boardRequest) {
        boardService.updateBoard(boardRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("success", "일기장 편집 성공");
        return ResponseEntity.ok(response);
    }
}
