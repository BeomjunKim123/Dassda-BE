package com.dassda.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikesResponse {
    @Schema(name = "닉네임")
    private String nickname;
    @Schema(name = "프로필 사진")
    private String profileUrl;
    @Schema(name = "좋아요 수")
    private int likeCount;
    private boolean likedByCurrentUser;

}
