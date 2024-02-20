package com.dassda.response;

import com.dassda.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeroResponse {

    @Schema(name = "닉네임")
    private String nickname;
    @Schema(description = "나를 제외한 인원 수")
    private int memberCount;
    @Schema(description = "공유하고 있는 일기 수")
    private int diaryCount;
    @Schema(description = "공유 일기 여부")
    private boolean hasSharedBoard;

    private boolean hasNewNotification;

    public HeroResponse(String nickname, int memberCount, int diaryCount, boolean hasSharedBoard, boolean hasNewNotification) {
        this.nickname = nickname;
        this.memberCount = memberCount;
        this.diaryCount = diaryCount;
        this.hasSharedBoard = hasSharedBoard;
        this.hasNewNotification = hasNewNotification;
    }
}
