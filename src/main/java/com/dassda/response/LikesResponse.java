package com.dassda.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikesResponse {

    private String nickname;
    private String profileUrl;
    private Integer likeCount;

}
