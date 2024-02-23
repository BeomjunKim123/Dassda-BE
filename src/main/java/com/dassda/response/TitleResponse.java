package com.dassda.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TitleResponse {

    private String boardTitle;

    public TitleResponse(String boardTitle) {
        this.boardTitle = boardTitle;
    }
}
