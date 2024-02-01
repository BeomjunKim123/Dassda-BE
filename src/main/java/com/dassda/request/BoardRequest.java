package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequest {

    @Schema
    private String title;
    @Schema
    private Integer imageNumber;
    @Schema
    private Integer appearanceType;

}
