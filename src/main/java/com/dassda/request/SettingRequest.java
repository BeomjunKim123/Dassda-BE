package com.dassda.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingRequest {

    private Long userId;
    private String fontSize;
    private String fontType;
}