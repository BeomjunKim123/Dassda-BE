package com.dassda.event;

import com.dassda.entity.Comment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class CommentCreatedEvent extends ApplicationEvent {

    private Comment comment;

    public CommentCreatedEvent(Object source, Comment comment) {
        super(source);
        this.comment = comment;
    }
}
