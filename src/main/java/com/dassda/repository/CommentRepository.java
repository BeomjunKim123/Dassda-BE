package com.dassda.repository;

import com.dassda.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByDiaryId(Long diaryId);

    List<Comment> findByDiaryId(Long diaryId);
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId ORDER BY c.regDate DESC")
    Optional<Comment> findLatestByCommentId(Long commentId);
}
