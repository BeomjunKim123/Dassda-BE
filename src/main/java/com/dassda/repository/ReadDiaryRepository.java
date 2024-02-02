package com.dassda.repository;

import com.dassda.entity.Diary;
import com.dassda.entity.ReadDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReadDiaryRepository extends JpaRepository<ReadDiary, Long> {
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM ReadDiary d WHERE d.diary.id = :diaryId AND d.readId = :readId")
    boolean existsByDiaryAndReadId(Long diaryId, Long readId);

}
