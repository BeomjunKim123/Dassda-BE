package com.dassda.response;

import com.dassda.entity.DiaryImg;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class DiaryDetailResponse {
    private Long id;
//    private Member writer;
    private Long writerId;
    private String nickname;
    private String profileUrl;

    private Long emotionId;
    private List<DiaryImg> images;
    private String title;
    private String contents;
    private LocalDateTime regDate;
    private Integer likeCount;
    private Integer commentCount;
    private boolean isOwned;

}
