package com.dassda.service;

import com.dassda.entity.Diary;
import com.dassda.entity.Member;
import com.dassda.entity.ReadDiary;
import com.dassda.repository.*;
import com.dassda.response.CalenderDayResponse;
import com.dassda.response.CalenderMonthResponse;
import com.dassda.response.ModeDiaryResponse;
import com.dassda.response.NewExistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeService {

    private final DiaryRepository diaryRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final ReadDiaryRepository readDiaryRepository;
    private final DiaryImgRepository diaryImgRepository;
    private final MemberRepository memberRepository;

    private Member member() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않음")
                );
    }

    public CalenderMonthResponse getMonthOfExistDiary(Long boardId, String date) {
        LocalDate month = LocalDate.parse(date);
        List<String> monthList = diaryRepository.findDiaryDatesByMonth(boardId, month.getMonthValue());
        CalenderMonthResponse calenderMonthResponse = new CalenderMonthResponse();
        calenderMonthResponse.setDateList(monthList);
        return calenderMonthResponse;
    }

    //1급 컬렉션도 사용해보자
    public List<CalenderDayResponse> getDayOfDiary(Long boardId, String date) {
        LocalDate day = LocalDate.parse(date);
        List<CalenderDayResponse> list = new ArrayList<>();
        List<Diary> diaryList = diaryRepository.findByBoardIdAndDay(boardId, day);
        for(Diary diary : diaryList) {
            CalenderDayResponse calenderDayResponse = new CalenderDayResponse();
            calenderDayResponse.setId(diary.getId());
            calenderDayResponse.setMemberId(diary.getMember().getId());
            calenderDayResponse.setNickname(diary.getMember().getNickname());
            calenderDayResponse.setBoardId(diary.getBoard().getId());
            calenderDayResponse.setEmotionId(diary.getSticker().getId());
            Long diaryId = diary.getId();
            String diaryImg = diaryImgRepository.findFirstByDiaryIdLimit(diaryId);
            String thumbUrl = "http://118.67.143.25:8080" + diaryImg;
            calenderDayResponse.setThumbnailUrl(thumbUrl);
            calenderDayResponse.setTitle(diary.getDiaryTitle());

            int likeCount = likesRepository.countByDiaryId(diaryId);
            calenderDayResponse.setLikeCount(likeCount);

            int commentCount = commentRepository.countByDiaryId(diaryId);
            calenderDayResponse.setCommentCount(commentCount);

            calenderDayResponse.setSelectedDate(diary.getSelectDate());

            String time = diaryRepository.findDiaryWithTimeAge(diaryId);
            calenderDayResponse.setTimeStamp(time);

            list.add(calenderDayResponse);

        }

        return list;
    }

    public NewExistResponse existNewDiary(Long boardId) {
        NewExistResponse newExistResponse = new NewExistResponse();
        Long memberId = member().getId();
        if (diaryRepository.existsByBoardIdAndMemberId(boardId, memberId)) {
            newExistResponse.setNexExist(true);
        } else {
            newExistResponse.setNexExist(false);
        }
        return newExistResponse;
    }

    public List<ModeDiaryResponse> getReadNotDiary(Long boardId) {
        List<ModeDiaryResponse> modeDiaryResponseList = new ArrayList<>();
        List<Diary> diaryList = diaryRepository.findSharedDiariesNotRead(boardId);
        for(Diary diary : diaryList) {
            ModeDiaryResponse modeDiaryResponse = new ModeDiaryResponse();
            modeDiaryResponse.setId(diary.getId());
            modeDiaryResponse.setMemberId(diary.getMember().getId());
            modeDiaryResponse.setNickname(diary.getMember().getNickname());
            modeDiaryResponse.setBoardId(diary.getBoard().getId());
            modeDiaryResponse.setEmotionId(diary.getSticker().getId());

            Long diaryId = diary.getId();
            String diaryImg = diaryImgRepository.findFirstByDiaryIdLimit(diaryId);
            String thumbUrl = "http://118.67.143.25:8080" + diaryImg;
            modeDiaryResponse.setThumbnailUrl(thumbUrl);
            modeDiaryResponse.setTitle(diary.getDiaryTitle());

            int likeCount = likesRepository.countByDiaryId(diaryId);
            modeDiaryResponse.setLikeCount(likeCount);

            int commentCount = commentRepository.countByDiaryId(diaryId);
            modeDiaryResponse.setCommentCount(commentCount);

            modeDiaryResponse.setRegDate(diary.getRegDate());
            modeDiaryResponse.setSelectDate(diary.getSelectDate());

            String time = diaryRepository.findDiaryWithTimeAge(diaryId);
            modeDiaryResponse.setTimeStamp(time);
            modeDiaryResponseList.add(modeDiaryResponse);
        }

        return modeDiaryResponseList;
    }

    public List<ModeDiaryResponse> getAllDiary(Long boardId, int pageSize, int lastViewId) {
        List<ModeDiaryResponse> modeDiaryResponseList = new ArrayList<>();
        List<Diary> diaryList = diaryRepository.findAllDiariesSortedByDateAndTime(boardId);


        //마지막 id 값이 9 라면 다음 무한 스크롤에서는 9부터 시작이 아니라 다음 일기부터 시작

        int startIndex = Math.max(lastViewId, 0);
        int endIndex = Math.min(startIndex + pageSize, diaryList.size());
        for (int i = startIndex; i < endIndex; i++) {
            ModeDiaryResponse modeDiaryResponse = new ModeDiaryResponse();
            modeDiaryResponse.setId(diaryList.get(i).getId());
            modeDiaryResponse.setMemberId(diaryList.get(i).getMember().getId());
            modeDiaryResponse.setNickname(diaryList.get(i).getMember().getNickname());
            modeDiaryResponse.setBoardId(diaryList.get(i).getBoard().getId());
            modeDiaryResponse.setEmotionId(diaryList.get(i).getSticker().getId());

            Long diaryId = diaryList.get(i).getId();
            String diaryImg = diaryImgRepository.findFirstByDiaryIdLimit(diaryId);
            String thumbUrl = "http://118.67.143.25:8080" + diaryImg;
            modeDiaryResponse.setThumbnailUrl(thumbUrl);
            modeDiaryResponse.setTitle(diaryList.get(i).getDiaryTitle());

            int likeCount = likesRepository.countByDiaryId(diaryId);
            modeDiaryResponse.setLikeCount(likeCount);

            int commentCount = commentRepository.countByDiaryId(diaryId);
            modeDiaryResponse.setCommentCount(commentCount);

            modeDiaryResponse.setRegDate(diaryList.get(i).getRegDate());
            modeDiaryResponse.setSelectDate(diaryList.get(i).getSelectDate());

            String time = diaryRepository.findDiaryWithTimeAge(diaryId);
            modeDiaryResponse.setTimeStamp(time);
            modeDiaryResponseList.add(modeDiaryResponse);
        }

        return modeDiaryResponseList;
    }

    public void setReadAllStatus(Long boardId) {

        List<Diary> diaryList = diaryRepository.findByBoardIdAndMemberIdNot(boardId, member().getId());

        for(Diary diary : diaryList) {
            if(!readDiaryRepository.existsByDiaryAndReadId(diary.getId(), member().getId())) {
                ReadDiary readDiary = new ReadDiary();
                readDiary.setDiary(diary);
                readDiary.setReadId(memberRepository.findById(member().getId()).orElseThrow(() -> new RuntimeException("멤버 없다.")));
                readDiaryRepository.save(readDiary);
            }
        }
    }
}
