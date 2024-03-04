package com.dassda.repository;

import com.dassda.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    int countByDiaryId(Long diaryId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Likes l WHERE l.member.id = :memberId AND l.diary.id = :diaryId")
    boolean existsMemberIdAndDiaryId(@Param(value = "memberId") Long memberId, @Param(value = "diaryId") Long diaryId);

    Optional<Likes> findByMemberIdAndDiaryId(Long memberId, Long diaryId);
    @EntityGraph(attributePaths = {"member"})
    List<Likes> findActiveLikesByDiaryId(@Param("diaryId") Long diaryId);

    boolean existsByDiaryIdAndMemberId(Long diaryId, Long memberId);
}
