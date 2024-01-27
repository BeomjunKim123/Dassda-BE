package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequest {
    @Schema
    private Long id;
    @Schema
    private String boardTitle;
    @Schema
    private Long imageNumber;
    @Schema
    private Long appearanceType;
}
