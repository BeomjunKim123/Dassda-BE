package com.dassda.repository;


import com.dassda.entity.ReadDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadDiaryRepository extends JpaRepository<ReadDiary, Long> {
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM ReadDiary d WHERE d.diary.id = :diaryId AND d.readId = :readId")
    boolean existsByDiaryAndReadId(Long diaryId, Long readId);
    @Query("SELECT COUNT(d) > 0 FROM Diary d " +
            "LEFT JOIN ReadDiary rd ON d.id = rd.diary.id " +
            "WHERE d.board.id = :boardId AND d.member.id != :memberId " +
            "AND rd.id IS NULL")
    boolean existsUnreadDiaries(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}
