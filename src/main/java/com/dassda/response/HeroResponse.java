package com.dassda.response;

import com.dassda.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "히어로 섹션 조회 응답 데이터")
public class HeroResponse {

    private String nickname;
    private Long memberCount;
    private Long diaryCount;
    private boolean isShared;

}
