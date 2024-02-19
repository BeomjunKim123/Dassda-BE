package com.dassda.controller;

import com.dassda.request.BoardRequest;
import com.dassda.response.BoardResponse;
import com.dassda.response.HeroResponse;
import com.dassda.response.MembersResponse;
import com.dassda.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@CrossOrigin
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "일기장 추가 API", description = "일기장 추가")
    @PostMapping()
    public ResponseEntity<Void> addBoard(@Valid @RequestBody BoardRequest boardRequest) {
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
    @GetMapping("/hero")
    public ResponseEntity<HeroResponse> getHero() {
        HeroResponse heroResponse = boardService.getHero();
        return ResponseEntity.ok(heroResponse);
    }

    @Operation(summary = "공유 일기장 멤버 조회 API", description = "boardId 에 대한 멤버들 정보 조회")
    @GetMapping("/{boardId}/members")
    public ResponseEntity<List<MembersResponse>> getMembers(
            @PathVariable(value = "boardId") Long boardId
            ) {
        List<MembersResponse> memberList = boardService.getMembers(boardId);
        return ResponseEntity.ok(memberList);
    }

    @Operation(summary = "일기장 나가기 API", description = "공유 일기장 나가기")
    @PostMapping("/{boardId}/resign")
    public ResponseEntity<Void> deleteShare(@PathVariable("boardId") Long boardId) {
        boardService.deleteShare(boardId);
        return ResponseEntity.ok().build();
    }
}
