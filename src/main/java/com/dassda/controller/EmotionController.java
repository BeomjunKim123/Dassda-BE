package com.dassda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prediction/emotion")
public class EmotionController {


    @Operation(summary = "기분 예측 API", description = "2주~4주까지는 예측이 어려움. 후에는 요일별로 통계를 내서 기분을 예측하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "기분 예측 성공"),
            @ApiResponse(responseCode = "fail", description = "예측 실패")
    })
    @GetMapping()
    public ResponseEntity<?> predictEmotion() {
        return null;
    }

}
