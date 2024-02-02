package com.dassda.repository;

import com.dassda.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByDiaryId(Long diaryId);
}
