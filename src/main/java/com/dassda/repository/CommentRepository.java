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
}
