package com.dassda.repository;

import com.dassda.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Integer countByDiaryId(Long diaryId);
}
