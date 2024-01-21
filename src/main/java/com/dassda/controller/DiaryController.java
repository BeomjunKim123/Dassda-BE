package com.dassda.controller;

import com.dassda.entity.Diary;
import com.dassda.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {
    private final DiaryService diaryService;
    @PostMapping()
    public ResponseEntity<Diary> addDiary() throws Exception {
        return null;
    }
    @GetMapping()
    public ResponseEntity<List<Diary>> getDiary(@RequestParam Long boardId) {
        return null;
    }
}
