package com.dassda.response;

import com.dassda.entity.DiaryImg;
import com.dassda.request.DiaryImgRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class DiaryDetailResponse {
    private Long id;
    private Long memberId;
    private String nickname;
    private String profileUrl;
    private Long emotionId;
    private List<DiaryImgRequest> images;
    private String title;
    private String contents;
    private LocalDateTime regDate;
    private LocalDateTime selectDate;
    private int likeCount;
    private int commentCount;
    private boolean isOwned;

}
