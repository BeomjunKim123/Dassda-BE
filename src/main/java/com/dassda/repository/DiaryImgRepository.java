package com.dassda.repository;

import com.dassda.entity.DiaryImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryImgRepository extends JpaRepository<DiaryImg, Long> {

    List<DiaryImg> findByDiaryId(Long diaryId);

//    Optional<DiaryImg> findFirstByDiaryId(Long diaryId, Pageable pageable);

    @Query(value = "SELECT d.img_url FROM diary_img d WHERE d.diary_id = :diaryId ORDER BY d.id ASC LIMIT 1", nativeQuery = true)
    String findFirstByDiaryIdLimit(@Param("diaryId") Long diaryId);

}
