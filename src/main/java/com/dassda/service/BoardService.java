package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Share;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.ReadDiaryRepository;
import com.dassda.repository.ShareRepository;
import com.dassda.request.BoardRequest;
import com.dassda.response.BoardResponse;
import com.dassda.response.HeroResponse;
import com.dassda.response.MembersResponse;
import com.dassda.utils.GetMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final DiaryRepository diaryRepository;
    private final ShareRepository shareRepository;
    private final ReadDiaryRepository readDiaryRepository;

    private Member currentMember() {
        return GetMember.getCurrentMember();
    }
    private void validateTitleLength(String title) {
        if (title.length() > 10) {
            throw new IllegalArgumentException("제목은 10자를 넘을 수 없다.");
        }
    }
    @Transactional
    public void addBoard(BoardRequest boardRequest) {
        validateTitleLength(boardRequest.getTitle());
        Board board = new Board(currentMember(), boardRequest.getTitle(), boardRequest.getImageNumber(),
                boardRequest.getAppearanceType(), LocalDateTime.now(), false, false);
        boardRepository.save(board);
    }
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("삭제된 일기장입니다."));
        board.setBackUp(true);
        boardRepository.save(board);
    }
    public List<BoardResponse> getBoard() {
        List<Board> boards = boardRepository.findByMemberId(currentMember().getId());
        return boards.stream()
                .filter(board -> !board.isBackUp())
                .map(this::convertToBoard)
                .collect(Collectors.toList());
    }
    private BoardResponse convertToBoard(Board board) {
        return new BoardResponse(board.getId(), board.getImageNumber(), board.getAppearanceType(),
                board.getTitle(), board.getRegDate(), diaryRepository.countByBoardId(board.getId()),
                Math.toIntExact(shareRepository.countByBoardId(board.getId())), newBadge(board.getId()), board.isShared());
    }
    private boolean newBadge(Long boardId) {
        Long memberId = currentMember().getId();
        return readDiaryRepository.existsUnreadDiaries(boardId, memberId);
    }
    @Transactional
    public void updateBoard(Long boardId, BoardRequest boardRequest) {
        validateTitleLength(boardRequest.getTitle());
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("삭제된 일기장입니다."));
        board.updateDetails(boardRequest.getTitle(), boardRequest.getImageNumber(), boardRequest.getAppearanceType());
        boardRepository.save(board);
    }
    public HeroResponse getHero() {
        Member member = currentMember();
        return new HeroResponse(member.getNickname(), Math.toIntExact(shareRepository.countByMemberId(member.getId())),
                diaryRepository.countIsSharedDiaries(member.getId()), boardRepository.existsSharedBoardByMemberId(member.getId()));
    }

    public List<MembersResponse> getMembers(Long boardId) {
        return shareRepository.findByBoardIdAboutMembers(boardId).stream()
                .map(share -> new MembersResponse(share.getMember().getNickname(), share.getMember().getProfile_image_url(), share.getRegDate()))
                .collect(Collectors.toList());
    }

    public void deleteShare(Long boardId) {
        Long memberId = currentMember().getId();
        Share share = shareRepository.findByBoardIdAndMemberId(boardId, memberId);
        shareRepository.delete(share);
    }
}

