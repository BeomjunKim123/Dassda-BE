package com.dassda.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareRequest {

    private Long boardId;
    private Long memberId;
    private String name;
    private String profileImageUrl;
    private String title;
}