package com.dassda.event;

import com.dassda.entity.Share;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewMemberEvent extends ApplicationEvent {
    private Share share;

    public NewMemberEvent(Object source, Share share) {
        super(source);
        this.share = share;
    }
}
