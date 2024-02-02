package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ShareRepository;
import com.dassda.request.BoardRequest;
import com.dassda.response.BoardResponse;
import com.dassda.response.HeroResponse;
import lombok.RequiredArgsConstructor;
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

    private Member member() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return memberRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new IllegalStateException("다시 로그인 해주세요.")
                );
    }
    public void addBoard(BoardRequest boardRequest) {
        Optional<Member> member = memberRepository.findByEmail(member().getEmail());

        if(boardRequest.getTitle().length() > 10) {
            throw new IllegalArgumentException("제목은 10자를 넘을 수 없다.");
        }

        if(member.isPresent()) {
            Board board = new Board();
            board.setMember(member.get());
            board.setTitle(boardRequest.getTitle());
            board.setImageNumber(boardRequest.getImageNumber());
            board.setAppearanceType(boardRequest.getAppearanceType());
            board.setRegDate(LocalDateTime.now());
            board.setShared(false);
            board.setBackUp(false);
            boardRepository.save(board);
        } else {
            throw new UsernameNotFoundException("다시 로그인 해주세요.");
        }
    }
    public void deleteBoard(Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if(boardOptional.isPresent()) {
            Board board = boardOptional.get();
            board.setBackUp(true);
            boardRepository.save(board);
        } else {
            throw new IllegalStateException("삭제된 일기장입니다.");
        }
    }
    public List<BoardResponse> getBoard() {
        Optional<Member> member = memberRepository.findByEmail(member().getEmail());

        if(member.isPresent()) {
            Long memberId = member.get().getId();
            List<Board> boards = boardRepository.findByMemberId(memberId);
            return boards.stream()
                    .filter(board -> !board.isBackUp())
                    .map(this::convertToBoard)
                    .collect(Collectors.toList());
        } else {
            throw new UsernameNotFoundException("다시 로그인 해주세요.");
        }
    }
    private BoardResponse convertToBoard(Board board) {
        Integer diaryCount = diaryRepository.countByBoardId(board.getId());
        Integer memberCount = shareRepository.countByBoardId(board.getId());
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
    private boolean newBadge() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(3);
        Boolean badge = diaryRepository.existsDiariesInLastThreeDays(startDate, endDate);
        return badge;
    }
    public void updateBoard(Long boardId, BoardRequest boardRequest) {
        Optional<Board> boardInfo = boardRepository.findById(boardId);

        if(boardRequest.getTitle().length() > 10) {
            throw new IllegalArgumentException("제목은 10자를 넘을 수 없다.");
        }

        if(boardInfo.isPresent()) {
            Board board = boardInfo.get();
            board.setTitle(boardRequest.getTitle());
            board.setImageNumber(boardRequest.getImageNumber());
            board.setAppearanceType(boardRequest.getAppearanceType());
            boardRepository.save(board);
        } else {
            throw new IllegalStateException("삭제된 일기장입니다.");
        }
    }
    public HeroResponse getHero() {
        HeroResponse heroResponse = new HeroResponse();
        Optional<Member> member = memberRepository.findByEmail(member().getEmail());
        Long memberId = member.get().getId();
        heroResponse.setNickname(member.get().getNickname());
        int shareCount = shareRepository.countByMemberId(memberId);
        heroResponse.setMemberCount(shareCount);
        int diaryCount = diaryRepository.countIsSharedDiaries(memberId);
        heroResponse.setMemberCount(diaryCount);
        boolean isShared = boardRepository.existsSharedBoardByMemberId(memberId);
        heroResponse.setHasSharedBoard(isShared);
        return heroResponse;
    }
}

