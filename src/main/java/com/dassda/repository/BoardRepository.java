package com.dassda.repository;

import com.dassda.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByMemberId(Long memberId);
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Board b Where b.member.id = :memberId AND b.isShared = true")
    boolean existsSharedBoardByMemberId(@Param("memberId") Long memberId);

    Optional<Board>findByShareLinkHash(String shareLinkHash);
}
