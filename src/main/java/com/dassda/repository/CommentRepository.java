package com.dassda.repository;

import com.dassda.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByDiaryId(Long diaryId);

    List<Comment> findByDiaryId(Long diaryId);

}
