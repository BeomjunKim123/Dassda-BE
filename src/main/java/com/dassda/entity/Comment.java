package com.dassda.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member write;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", referencedColumnName = "id")
    private Diary diary;

    @Column(name = "comment")
    private String comment;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "back_up")
    private boolean backUp;

}
