package com.dassda.repository;

import com.dassda.entity.Diary;
import com.dassda.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d FROM Diary d WHERE d.member.id = :memberId AND d.board.id = :boardId AND FUNCTION('DATE', d.selectDate) = FUNCTION('DATE', :selectDate)")
    Optional<Diary> findByMemberIdAndBoardIdAndSelectDate(@Param(value = "memberId") Long memberId, @Param(value = "boardId") Long boardId, @Param(value = "selectDate") LocalDate selectDate);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.member.id = :memberId AND d.board.id = :boardId AND FUNCTION('DATE', d.selectDate) = FUNCTION('DATE', :selectDate) AND d.backUp = false")
    boolean exitsByDiaryAtSelectDate(@Param("memberId") Long memberId, @Param("boardId") Long boardId, @Param("selectDate") LocalDate selectDate);

    Integer countByBoardId(Long boardId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.member.id = :memberId")
    boolean existsWriterOwner(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(d) FROM Board b INNER JOIN Diary d ON b.id = d.board.id WHERE d.board.isShared = true AND d.board.member.id = :memberId")
    Integer countIsSharedDiaries(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.regDate BETWEEN :startDate AND :endDate")
    boolean existsDiariesInLastThreeDays(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT DISTINCT SUBSTRING(d.select_date, 1, 10) FROM diary d WHERE d.board_id = :boardId AND MONTH(d.select_date) = :month AND YEAR(d.select_date) = :year", nativeQuery = true)
    List<String> findDiaryDatesByMonthAndYear(@Param("boardId") Long boardId, @Param("month") int month, @Param("year") int year);


    @Query("SELECT d FROM Diary d WHERE d.board.id = :boardId AND FUNCTION('DATE', d.selectDate) = :day AND d.board.backUp = false AND d.backUp = false")
    List<Diary> findByBoardIdAndDay(@Param("boardId") Long boardId, @Param("day") LocalDate day);

    @Query("SELECT d FROM Diary d WHERE d.board.id = :boardId AND FUNCTION('DATE', d.selectDate) = :selectedDateTime AND d.board.backUp = false AND d.backUp = false")
    List<Diary> findByBoardIdAndSelectedDate(@Param("boardId") Long boardId, @Param("selectedDateTime") LocalDate selectedDateTime);
//    @Query(value = "SELECT " +
//            "CASE " +
//            "WHEN DATE(d.select_date) = CURDATE() THEN '오늘' " +
//            "WHEN DATE(d.select_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN '1일 전' " +
//            "WHEN DATE(d.select_date) > DATE_SUB(CURDATE(), INTERVAL 7 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), DATE(d.select_date))), '일 전') " +
//            "WHEN DATE(d.select_date) > DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), DATE(d.select_date)) / 7), '주 전') " +
//            "ELSE CONCAT(FLOOR(DATEDIFF(CURDATE(), DATE(d.select_date)) / 30), '개월 전') " +
//            "END " +
//            "FROM diary d WHERE d.id = :diaryId", nativeQuery = true)
//    String findDiaryWithTimeAge(@Param("diaryId") Long diaryId);

    @Query(value = "SELECT " +
            "CASE " +
            "WHEN TIMESTAMPDIFF(MINUTE, d.reg_date, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, d.reg_date, NOW()), '분 전') " +
            "WHEN TIMESTAMPDIFF(HOUR, d.reg_date, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, d.reg_date, NOW()), '시간 전') " +
            "WHEN DATE(d.reg_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN '1일 전' " +
            "WHEN DATE(d.reg_date) > DATE_SUB(CURDATE(), INTERVAL 7 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date)), '일 전') " +
            "WHEN DATE(d.reg_date) > DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date) / 7), '주 전') " +
            "ELSE CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date) / 30), '개월 전') " +
            "END " +
            "FROM diary d WHERE d.id = :diaryId", nativeQuery = true)
    String findDiaryWithTimeAge(@Param("diaryId") Long diaryId);

    @Query("SELECT d FROM Diary d WHERE d.board.isShared = true AND d.board.backUp = false AND d.backUp = false AND d.board.id = :boardId AND d.member.id != :memberId AND NOT EXISTS " +
            "(SELECT rd FROM ReadDiary rd WHERE rd.diary.id = d.id) ORDER BY d.selectDate DESC")
    List<Diary> findSharedDiariesNotRead(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query(value = "SELECT * FROM diary d WHERE d.board_id = :boardId AND d.back_up = false ORDER BY DATE(select_date) DESC, TIME(select_date) ASC", nativeQuery = true)
    List<Diary> findAllDiariesSortedByDateAndTime(@Param("boardId") Long boardId);

    @Query("SELECT CASE WHEN COUNT(rd) > 0 THEN true ELSE false END FROM ReadDiary rd WHERE rd.diary.board.id = :boardId AND rd.readId.id = :memberId")
    boolean existsByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);


    List<Diary> findByBoardIdAndMemberIdNot(Long boardId, Long memberId);
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.member.id = :memberId AND d.board.id = :boardId")
    boolean existsByMemberIdAndBoardId(@Param("boardId") Long boardId, @Param("memberId") Long memeberId);

    List<Diary> findByBoardId(Long boardId);

    List<Diary> findByMemberId(Long memberId);

}
