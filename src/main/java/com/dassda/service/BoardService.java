package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ShareRepository;
import com.dassda.request.BoardRequest;
import com.dassda.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final DiaryRepository diaryRepository;
    private final ShareRepository shareRepository;

    private Member email() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("다시 로그인 해주세요."));
    }
    public void addBoard(BoardRequest boardRequest) {
        Optional<Member> member = memberRepository.findByEmail(email().getEmail());

        if(member.isPresent()) {
            Board board = new Board();
            board.setMember(member.get());
            board.setTitle(boardRequest.getBoardTitle());
            board.setImageNumber(boardRequest.getImageNumber());
            board.setAppearanceType(boardRequest.getAppearanceType());
            board.setRegDate(LocalDateTime.now());
            board.setIsShared(0);
            board.setBackUp(0);
            boardRepository.save(board);
        } else {
            throw new UsernameNotFoundException("다시 로그인 해주세요.");
        }
    }
    public void deleteBoard(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);

        if(board.isPresent()) {
            boardRepository.delete(board.get());
        } else {
            throw new IllegalStateException("삭제된 일기장입니다.");
        }
    }
    public List<BoardResponse> getBoard() {
        Optional<Member> member = memberRepository.findByEmail(email().getEmail());

        if(member.isPresent()) {
            Long memberId = member.get().getId();
            List<Board> boards = boardRepository.findByMemberId(memberId);
            return boards.stream()
                    .map(this::convertToBoard)
                    .collect(Collectors.toList());
        } else {
            throw new UsernameNotFoundException("다시 로그인 해주세요.");
        }
    }
    private BoardResponse convertToBoard(Board board) {
        Long diaryCount = diaryRepository.countByBoardId(board.getId());
        int memberCount = shareRepository.countByBoardId(board.getId());
        BoardResponse boardResponse = new BoardResponse();
        boardResponse.setId(board.getId());
        boardResponse.setImageNumber(board.getImageNumber());
        boardResponse.setAppearanceType(board.getAppearanceType());
        boardResponse.setTitle(board.getTitle());
        boardResponse.setRegDate(board.getRegDate());
        boardResponse.setDiaryCount(diaryCount);
        boardResponse.setMemberCount(memberCount);
        boardResponse.setNewBadge(newBadge());
        return boardResponse;
    }
    private int newBadge() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(3);
        int badge = diaryRepository.existsDiariesInLastThreeDays(startDate, endDate);
        return badge;
    }
    public void updateBoard(BoardRequest boardRequest) {
        Optional<Board> boardInfo = boardRepository.findById(boardRequest.getId());

        if(boardInfo.isPresent()) {
            Board board = boardInfo.get();
            boardRepository.save(board);
        } else {
            throw new IllegalStateException("삭제된 일기장입니다.");
        }
    }
}

