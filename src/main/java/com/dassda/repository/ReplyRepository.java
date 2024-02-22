package com.dassda.repository;

import com.dassda.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByCommentId(Long commentId);
    @Query(value = "SELECT r.* FROM reply r INNER JOIN comment c ON r.comment_id = c.id " +
            "WHERE c.id = :commentId ORDER BY r.id ASC LIMIT 3", nativeQuery = true)
    List<Reply> findByCommentIdThree(@Param(value = "commentId") Long commentId);

    @Query(value = "SELECT r.* FROM reply r INNER JOIN comment c ON r.comment_id = c.id " +
            "WHERE c.id = :commentId ORDER BY r.id ASC LIMIT 100 OFFSET 3", nativeQuery = true)
    List<Reply> findByCommentIdExceptThree(@Param(value = "commentId") Long commentId);

    @Query(value = "SELECT " +
            "CASE " +
            "WHEN TIMESTAMPDIFF(MINUTE, d.reg_date, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, d.reg_date, NOW()), '분 전') " +
            "WHEN TIMESTAMPDIFF(HOUR, d.reg_date, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, d.reg_date, NOW()), '시간 전') " +
            "WHEN DATE(d.reg_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN '1일 전' " +
            "WHEN DATE(d.reg_date) > DATE_SUB(CURDATE(), INTERVAL 7 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date)), '일 전') " +
            "WHEN DATE(d.reg_date) > DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date) / 7), '주 전') " +
            "ELSE CONCAT(FLOOR(DATEDIFF(CURDATE(), d.reg_date) / 30), '개월 전') " +
            "END " +
            "FROM Reply d WHERE d.id = :replyId", nativeQuery = true)
    String findDiaryWithTimeAge(@Param("replyId") Long replyId);
}
