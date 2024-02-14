package com.dassda.event;

import com.dassda.entity.Reply;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReplyCreatedEvent extends ApplicationEvent {
    private Reply reply;

    public ReplyCreatedEvent(Object source, Reply reply) {
        super(source);
        this.reply = reply;
    }
}
