package com.dassda.repository;

import com.dassda.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    int countByBoardId(Long boardId);

    int countByMemberId(Long memberId);
    @Query("SELECT s FROM Share s WHERE s.board.id = :boardId")
    List<Share> findByBoardIdAboutMembers(@Param(value = "boardId") Long boardId);

    Share findByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    boolean existsById(Long memberId);

    Optional<Share> findByMemberIdAndBoardId(Long memberId, Long boardId);

    boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);
    List<Share> findByMemberId(Long currentMemberId);
}

