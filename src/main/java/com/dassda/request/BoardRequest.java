package com.dassda.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequest {
    private Long id;
    private String boardTitle;
    private int imageNumber;
    private int appearanceType;
}
