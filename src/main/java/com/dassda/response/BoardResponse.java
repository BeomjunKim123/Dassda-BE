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

    private Long id;
    private Integer imageNumber;
    private Integer appearanceType;
    private String title;
    private boolean isShared;
    private LocalDateTime regDate;
    private Integer diaryCount;
    private Integer memberCount;
    private boolean newBadge;

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