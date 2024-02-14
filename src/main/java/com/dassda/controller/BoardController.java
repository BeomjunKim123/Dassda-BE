package com.dassda.controller;

import com.dassda.request.BoardRequest;
import com.dassda.response.BoardResponse;
import com.dassda.response.HeroResponse;
import com.dassda.service.BoardService;
import com.dassda.service.ShareRedisService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@CrossOrigin("http://localhost:3000/**")
public class BoardController {

    private final BoardService boardService;
    private final ShareRedisService shareRedisService;

    @Operation(summary = "일기장 추가 API", description = "일기장 추가")
    @PostMapping()
    public ResponseEntity<Void> addBoard(@RequestBody BoardRequest boardRequest) {
        boardService.addBoard(boardRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "일기장 삭제 API", description = "일기장 아이디 값으로 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable(value = "boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "일기장 조회 API", description = "일기 개수, 3일 이내의 일기 존재 유무, 공유 일기장 유무 추가적인 데이터")
    @GetMapping()
    public ResponseEntity<List<BoardResponse>> getBoard() {
        List<BoardResponse> boards = boardService.getBoard();
        return ResponseEntity.ok(boards);
    }


    @Operation(summary = "일기장 편집 API", description = "일기장 편집 PUT")
    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable(value = "boardId") Long boardId, @RequestBody BoardRequest boardRequest) {
        boardService.updateBoard(boardId, boardRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "히어로 섹션 조회 API", description = "사용자 이름, 사람 수, 일기 개수, 공유 일기장 존재 유무")
    @GetMapping(value = "hero")
    public ResponseEntity<HeroResponse> getHero() {
        HeroResponse heroResponse = boardService.getHero();
        return ResponseEntity.ok(heroResponse);
    }
}
