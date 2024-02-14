package com.dassda.event;

import com.dassda.entity.Likes;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class LikeCreatedEvent extends ApplicationEvent {

    private Likes likes;

    public LikeCreatedEvent(Object source, Likes likes) {
        super(source);
        this.likes = likes;
    }
}
