package com.dassda.repository;

import com.dassda.entity.Diary;
import com.dassda.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByMember(Member member);
    List<Diary> findByRegDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    Optional<Diary> findByBoardId(Long boardId);

    @Query("SELECT d FROM Diary d JOIN FETCH d.member WHERE d.board.id = :boardId AND FUNCTION('DATE', d.regDate) = FUNCTION('DATE', :selectDate)")
    List<Diary> findByBoardIdAndRegDateWithFetchJoin(@Param("boardId") Long boardId, @Param("selectDate") LocalDateTime selectDate);

    Integer countByBoardId(Long boardId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.member.id = :memberId")
    boolean existsWriterOwner(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(d) FROM Board b INNER JOIN Diary d ON b.id = d.board.id WHERE d.board.isShared = true AND d.board.member.id = :memberId")
    Integer countIsSharedDiaries(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.regDate BETWEEN :startDate AND :endDate")
    boolean existsDiariesInLastThreeDays(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT SUBSTRING(d.reg_date, 1, 10) FROM diary d WHERE d.board_id = :boardId AND MONTH(d.reg_date) = :month", nativeQuery = true)
    List<String> findDiaryDatesByMonth(@Param("boardId") Long boardId, @Param("month") int month);

    @Query("SELECT d FROM Diary d WHERE d.board.id = :boardId AND FUNCTION('DATE', d.regDate) = :day")
    List<Diary> findByBoardIdAndDay(@Param("boardId") Long boardId, @Param("day") LocalDate day);

    @Query(value = "SELECT " +
            "CASE " +
            "WHEN DATE(d.select_date) = CURDATE() THEN '오늘' " +
            "WHEN DATE(d.select_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN '1일 전' " +
            "WHEN DATE(d.select_date) > DATE_SUB(CURDATE(), INTERVAL 7 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), DATE(d.select_date))), '일 전') " +
            "WHEN DATE(d.select_date) > DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), DATE(d.select_date)) / 7), '주 전') " +
            "ELSE CONCAT(FLOOR(DATEDIFF(CURDATE(), DATE(d.select_date)) / 30), '개월 전') " +
            "END " +
            "FROM Diary d WHERE d.id = :diaryId", nativeQuery = true)
    String findDiaryWithTimeAge(Long diaryId);

    @Query("SELECT d FROM Diary d WHERE d.board.isShared = false AND d.board.id = :boardId AND NOT EXISTS (" +
            "SELECT rd FROM ReadDiary rd WHERE rd.diary.id = d.id)")
    List<Diary> findSharedDiariesNotRead(Long boardId);

    @Query(value = "SELECT * FROM diary d WHERE d.board_id = :boardId ORDER BY DATE(select_date) DESC, TIME(select_date) ASC", nativeQuery = true)
    List<Diary> findAllDiariesSortedByDateAndTime(Long boardId);

    @Query("SELECT CASE WHEN COUNT(rd) > 0 THEN true ELSE false END FROM ReadDiary rd WHERE rd.diary.board.id = :boardId AND rd.readId.id = :memberId")
    boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);

    List<Diary> findByBoardIdAndMemberIdNot(Long boardId, Long memberId);


}
