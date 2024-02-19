package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Diary;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Diary> diaryList = diaryRepository.findByBoardId(board.getId());
        diaryList.stream()
                        .filter(diary -> !diary.isBackUp())
                        .forEach(diary -> diary.setBackUp(true));
        diaryRepository.saveAll(diaryList);
        boardRepository.save(board);
    }
    public List<BoardResponse> getBoard() {
        List<Board> boards = boardRepository.findByMemberId(currentMember().getId());
        return boards.stream()
                .filter(board -> !board.isBackUp())
                .map(this::convertToBoard)
                .collect(Collectors.toList());
    }

//    public List<BoardResponse> getBoard() {
//        Member currentMember = currentMember();
//        Long currentMemberId = currentMember.getId();
//
//        // 현재 사용자가 소유한 일기장 조회
//        List<Board> ownedBoards = boardRepository.findByMemberId(currentMemberId);
//
//        // 현재 사용자가 참여한 공유 일기장 조회
//        List<Share> shares = shareRepository.findByMemberId(currentMemberId);
//        List<Board> sharedBoards = shares.stream()
//                .map(Share::getBoard)
//                .collect(Collectors.toList());
//
//        // 소유한 일기장과 참여한 일기장을 합친 후 중복 제거
//        List<Board> allBoards = Stream.concat(ownedBoards.stream(), sharedBoards.stream())
//                .distinct()
//                .collect(Collectors.toList());
//
//        // 변환 로직을 통해 BoardResponse 리스트 생성 및 반환
//        return allBoards.stream()
//                .filter(board -> !board.isBackUp()) // 백업되지 않은 일기장만 필터링
//                .map(this::convertToBoard) // Board 객체를 BoardResponse DTO로 변환
//                .collect(Collectors.toList());
//    }
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

