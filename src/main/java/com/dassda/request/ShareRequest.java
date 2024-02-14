package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareRequest {
    @Schema(name = "일기장 아이디")
    private Long boardId;
    @Schema(name = "공유 보낸 사용자")
    private Long memberId;
    @Schema(name = "닉네임")
    private String name;
    @Schema(name = "프로필 사진")
    private String profileImageUrl;
    @Schema(name = "일기장 이름")
    private String title;
}