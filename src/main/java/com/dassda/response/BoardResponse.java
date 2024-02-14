package com.dassda.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    @Schema(name = "일기장")
    private Long id;
    @Schema(name = "일기장 디자인")
    private Integer imageNumber;
    @Schema(name = "일기장 스타일")
    private Integer appearanceType;
    @Schema(name = "일기장 제목")
    private String title;
    @Schema(name = "공유 일기장 상태")
    private boolean isShared;
    @Schema(name = "일기장 생성일")
    private LocalDateTime regDate;
    @Schema(name = "일기 개수")
    private Integer diaryCount;
    @Schema(name = "사용자 수")
    private Integer memberCount;
    @Schema(name = "안 읽은 일기 유무")
    private boolean newBadge;

}