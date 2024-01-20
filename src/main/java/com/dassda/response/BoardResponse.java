package com.dassda.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
//@RequiredArgsConstructor 필드에서 final로 선언된 필드만 포함하는 생성자 생성
@AllArgsConstructor //모든 필드를 포함하는 생성자 생성
@NoArgsConstructor  //인자 없는 생성자를 생성
public class BoardResponse {
    private Long id;
    private int imageNumber;
    private int appearanceType;
    private String title;
    private int isShared;
    private LocalDateTime regDate;
    private Long diaryCount;
    private int memberCount;
    private int newBadge;
    private int backUp;
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