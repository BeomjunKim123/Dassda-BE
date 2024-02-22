package com.dassda.repository;

import com.dassda.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Integer countByDiaryId(Long diaryId);

    List<Comment> findByDiaryId(Long diaryId);
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId ORDER BY c.regDate DESC")
    Optional<Comment> findLatestByCommentId(Long commentId);


    @Query(value = "select r.* from comment c inner join reply r on c.id = r.comment_id where c.diary_id = :diaryId", nativeQuery = true)
    Integer countRepliesByDiaryId(@Param("diaryId") Long diaryId);

    @Query(value = "SELECT " +
            "CASE " +
            "WHEN TIMESTAMPDIFF(MINUTE, d.reg_date, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, d.reg_date, NOW()), '분 전') " +
            "WHEN TIMESTAMPDIFF(HOUR, d.reg_date, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, d.reg_date, NOW()), '시간 전') " +
            "WHEN DATE(d.reg_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN '1일 전' " +
            "WHEN DATE(d.reg_date) > DATE_SUB(CURDATE(), INTERVAL 7 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date)), '일 전') " +
            "WHEN DATE(d.reg_date) > DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date) / 7), '주 전') " +
            "ELSE CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date) / 30), '개월 전') " +
            "END " +
            "FROM comment d WHERE d.id = :commentId", nativeQuery = true)
    String findDiaryWithTimeAge(@Param("commentId") Long commentId);
}
