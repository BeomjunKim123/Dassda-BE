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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final NotificationService notificationService;

    private Member currentMember() {
        return GetMember.getCurrentMember();
    }

    @Transactional
    public void addBoard(BoardRequest boardRequest) {
        Board board = new Board(currentMember(), boardRequest.getTitle(), boardRequest.getImageNumber(),
                boardRequest.getAppearanceType(), LocalDateTime.now(), false, false);
        board = boardRepository.save(board);
        Share share = new Share(currentMember(), board, LocalDateTime.now());
        shareRepository.save(share);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("삭제된 일기장입니다."));
        if(!board.getMember().getId().equals(currentMember().getId())) {
            throw new IllegalStateException("삭제 권한이 없다.");
        }
        board.setBackUp(true);
        List<Diary> diaryList = diaryRepository.findByBoardId(board.getId());
        diaryList.stream()
                        .filter(diary -> !diary.isBackUp())
                        .forEach(diary -> diary.setBackUp(true));
        diaryRepository.saveAll(diaryList);
        boardRepository.save(board);
    }
    public List<BoardResponse> getBoard() {
        Long currentMemberId = currentMember().getId();
        List<Board> ownBoards = boardRepository.findByMemberId(currentMemberId);
        List<Board> sharedBoards = shareRepository.findBoardsShared(currentMemberId);

        List<Board> allBoards = Stream.concat(ownBoards.stream(), sharedBoards.stream())
                .distinct()
                .filter(board -> !board.isBackUp())
                .collect(Collectors.toList());

        return allBoards.stream()
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
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("삭제된 일기장입니다."));

        board.updateDetails(boardRequest.getTitle(), boardRequest.getImageNumber(), boardRequest.getAppearanceType());
        boardRepository.save(board);
    }
    public HeroResponse getHero() {
        Long memberId = currentMember().getId();
        return new HeroResponse(currentMember().getNickname(),
                shareRepository.countByMemberIdAboutShare(memberId) - boardRepository.countByMemberId(memberId),
                diaryRepository.countIsSharedDiaries(memberId),
                boardRepository.existsSharedBoardByMemberId(memberId),
                notificationService.existsNotification()
        );
    }

    public List<MembersResponse> getMembers(Long boardId) {
        return shareRepository.findByBoardIdAboutMembers(boardId).stream()
                .map(share -> new MembersResponse(
                        share.getMember().getId(),
                        share.getMember().getNickname(),
                        share.getMember().getProfile_image_url(),
                        share.getRegDate()))
                .collect(Collectors.toList());
    }

    public void deleteShare(Long boardId) {
        Long memberId = currentMember().getId();
        Share share = shareRepository.findByBoardIdAndMemberId(boardId, memberId);
        Board board = boardRepository.findByMemberIdAndId(memberId, boardId);
        if(memberId.equals(board.getMember().getId())) {
            board.setBackUp(true);
            boardRepository.save(board);
        }
        shareRepository.delete(share);
    }
}

