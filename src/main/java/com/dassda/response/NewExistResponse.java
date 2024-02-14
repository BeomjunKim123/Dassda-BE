package com.dassda.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewExistResponse {
    @Schema(name = "")
    private boolean nexExist;

}
