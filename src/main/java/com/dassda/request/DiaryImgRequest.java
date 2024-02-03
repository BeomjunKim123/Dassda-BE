package com.dassda.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryImgRequest {
    private Long id;
    private String imgUrl;
}
