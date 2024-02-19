package com.dassda.controller;

import com.dassda.response.EmotionResponse;
import com.dassda.service.EmotionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prediction/emotion")
@CrossOrigin
public class EmotionController {

    private final EmotionService emotionService;

    @Operation(summary = "기분 예측 API", description = "2주~4주까지는 예측이 어려움. 후에는 요일별로 통계를 내서 기분을 예측하기")
    @GetMapping()
    public ResponseEntity<EmotionResponse> predictEmotion() {
        return ResponseEntity.ok(emotionService.getRandomResponse());
    }

}
