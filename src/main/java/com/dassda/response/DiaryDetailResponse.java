package com.dassda.response;

import com.dassda.entity.DiaryImg;
import com.dassda.request.DiaryImgRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DiaryDetailResponse {

    private Long id;

    private Long memberId;

    private String nickname;

    private String profileUrl;

    private Long emotionId;
    @Schema(ref = "#/components/schemas/DiaryImgRequest")
    private List<DiaryImgRequest> images;

    private String title;

    private String contents;
    @Schema(name = "작성일부터 과거로 일수")
    private String timeStamp;

    private LocalDateTime selectedDate;

    private int likeCount;

    private int commentCount;

    private boolean isOwned;

    private boolean isLiked;

    public DiaryDetailResponse(Long id, Long memberId, String nickname, String profileUrl, Long emotionId,
                               List<DiaryImgRequest> images, String title, String contents, String timeStamp,
                               LocalDateTime selectedDate, int likeCount, int commentCount, boolean isOwned, boolean isLiked) {
        this.id = id;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.emotionId = emotionId;
        this.images = images;
        this.title = title;
        this.contents = contents;
        this.timeStamp = timeStamp;
        this.selectedDate = selectedDate;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isOwned = isOwned;
        this.isLiked = isLiked;
    }

}
