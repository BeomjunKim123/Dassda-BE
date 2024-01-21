package com.dassda.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "일기장 조회 응답 데이터")
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private Long id;
    private int imageNumber;
    private int appearanceType;
    private String title;
    private boolean isShared;
    private LocalDateTime regDate;
    private Long diaryCount;
    private int memberCount;
    private boolean newBadge;
    private boolean backUp;
}
//빌더 패턴
//    private BoardResponse convertToBoardResponse(Board board) {
//        Long diaryCount = diaryRepository.countByBoardId(board.getId());
//        return BoardResponse.builder()
//                .id(board.getId())
//                .imageNumber(board.getImageNumber())
//                .appearanceType(board.getAppearanceType())
//                .title(board.getTitle())
//                .isShared(board.isShared())
//                .regDate(board.getRegDate())
//                .diaryCount(diaryCount)
//                .build();
//    }