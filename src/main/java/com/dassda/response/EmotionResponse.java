package com.dassda.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmotionResponse {
    private String emotionContent;

    public EmotionResponse(String emotionContent) {
        this.emotionContent = emotionContent;
    }
}
