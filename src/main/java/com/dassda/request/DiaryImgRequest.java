package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryImgRequest {
    @Schema(name = "일기 사진")
    private Long id;
    @Schema(name = "일기 사진 경로")
    private String imgUrl;
}
