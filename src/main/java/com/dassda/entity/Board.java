package com.dassda.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Column(name = "design")
    private Integer imageNumber;

    @Column(name = "style")
    private Integer appearanceType;

    @Column(name = "board_title")
    private String title;

    @Column(name = "share")
    private boolean isShared;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "back_up")
    private boolean backUp;
}
