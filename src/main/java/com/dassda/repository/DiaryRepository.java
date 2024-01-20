package com.dassda.repository;

import com.dassda.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByRegDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    Optional<Diary> findByBoardId(Long boardId);
    Long countByBoardId(Long boardId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN 1 ELSE 0 END FROM Diary d WHERE d.regDate BETWEEN :startDate AND :endDate")
    int existsDiariesInLastThreeDays(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
