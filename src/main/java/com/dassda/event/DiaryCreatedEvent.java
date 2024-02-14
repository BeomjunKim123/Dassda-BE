package com.dassda.event;

import com.dassda.entity.Diary;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class DiaryCreatedEvent extends ApplicationEvent {
    private Diary diary;

    public DiaryCreatedEvent(Object source, Diary diary) {
        super(source);
        this.diary = diary;
    }
}
