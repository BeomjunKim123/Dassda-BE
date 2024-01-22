package com.dassda.repository;

import com.dassda.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Long countByBoardId(Long boardId);
    Long countByMemberId(Long memberId);
}
