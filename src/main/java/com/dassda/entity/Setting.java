package com.dassda.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Setting {

    @Id
    private Long userId;
    private String fontSize;
    private String fontType;
}
