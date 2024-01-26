package com.dassda.repository;

import com.dassda.entity.DiaryImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryImgRepository extends JpaRepository<DiaryImg, Long> {

    List<DiaryImg> findByDiaryId(Long diaryId);
}
