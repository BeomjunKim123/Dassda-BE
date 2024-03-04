package com.dassda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "board", indexes = {
        @Index(name = "idx_member_id", columnList = "member_id"),
        @Index(name = "idx_back_up", columnList = "back_up")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "share_link")
    private String shareLinkHash;

    @Column(name = "share")
    private boolean isShared;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "back_up")
    private boolean backUp;

    public Board(Member member, String title, Integer imageNumber, Integer appearanceType, LocalDateTime regDate, boolean isShared, boolean backUp) {
        this.member = member;
        this.title = title;
        this.imageNumber = imageNumber;
        this.appearanceType = appearanceType;
        this.regDate = regDate;
        this.isShared = isShared;
        this.backUp = backUp;
    }

    public void updateDetails(String title, Integer imageNumber, Integer appearanceType) {
        this.title = title;
        this.imageNumber = imageNumber;
        this.appearanceType = appearanceType;
    }
}
