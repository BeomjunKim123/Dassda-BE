package com.dassda.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "diary_img")
@Getter
@Setter
public class DiaryImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", referencedColumnName = "id")
    private Diary diary;

    @Column(name = "img_name")
    private String imgName;

    @Column(name = "ori_img_name")
    private String oriImgName;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "back_up")
    private boolean backUp;

    public void updateDiaryImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
