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
}
