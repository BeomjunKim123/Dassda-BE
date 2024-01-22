package com.dassda.repository;

import com.dassda.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByRegDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    Optional<Diary> findByBoardId(Long boardId);
    Long countByBoardId(Long boardId);

    @Query("SELECT COUNT(d) FROM Board b INNER JOIN Diary d ON b.id = d.board.id WHERE d.board.isShared = true AND d.board.member.id = :memberId")
    Long countIsSharedDiaries(@Param("memberId") Long memberId);

//    @Query("SELECT COUNT(d) FROM Diary d INNER JOIN Board b ON d.board_id = b.id WHERE b.is_shard = 1 AND b.member.id = :memberId")
//    int countIsSharedDiaries(@Param("memberId") Long memberId);
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.regDate BETWEEN :startDate AND :endDate")
    boolean existsDiariesInLastThreeDays(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Query("SELECT DISTINCT SUBSTRING(CAST(d.regDate AS string), 1, 10) FROM Diary d WHERE SUBSTRING(CAST(d.regDate AS string), 1, 7) = :month")
    List<String> findDiaryDatesByMonth(@Param("month") String month);
//    @Query("SELECT DISTINCT FUNCTION('DATE_FORMAT', d.regDate, '%Y-%m-%d') FROM Diary d WHERE d.regDate BETWEEN :startDate AND :endDate")
//    List<String> findDiaryDatesByMonth2(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//    List<Diary> findByRegDateAndBoardId(LocalDate regDate, Long boardId);
    @Query("SELECT d FROM Diary d WHERE d.board.id = :boardId AND FUNCTION('DATE', d.regDate) = :day")
    List<Diary> findByBoardIdAndDay(@Param("boardId") Long boardId, @Param("day") LocalDate day);



}
