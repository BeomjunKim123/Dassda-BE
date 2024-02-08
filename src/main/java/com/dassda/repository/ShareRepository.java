package com.dassda.repository;

import com.dassda.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    int countByBoardId(Long boardId);

    int countByMemberId(Long memberId);

    Optional<Share> findByHashValue(String hashValue);
    @Query("SELECT s FROM Share s WHERE s.board.id = :boardId")
    Optional<Share> findByBoardIdAndExpiryDateAfter(@Param(value = "boardId") Long boardId);
}

