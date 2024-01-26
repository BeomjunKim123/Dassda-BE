package com.dassda.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "diary")
@Getter
@Setter
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "write_id", referencedColumnName = "id")
    private Member writes;

    @OneToOne
    @JoinColumn(name = "sticker_id", referencedColumnName = "id")
    private Sticker sticker;

    @Column(name = "diary_title")
    private String diaryTitle;

    @Column(name = "content")
    private String diaryContent;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "back_up")
    private boolean backUp;

}
