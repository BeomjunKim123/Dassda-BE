package com.dassda.service;

import com.dassda.entity.Diary;
import com.dassda.repository.*;
import com.dassda.response.CalenderDayResponse;
import com.dassda.response.CalenderMonthResponse;
import com.dassda.response.ModeDiaryResponse;
import com.dassda.response.NewExistResponse;
import lombok.RequiredArgsConstructor;
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
    private final MemberRepository memberRepository;
    private final DiaryImgRepository diaryImgRepository;

    public CalenderMonthResponse getMonth(Long boardId, String date) {

        LocalDate month = LocalDate.parse(date);

        List<String> monthList = diaryRepository.findDiaryDatesByMonth(boardId, month.getMonthValue());
        System.out.println(monthList);
        CalenderMonthResponse calenderMonthResponse = new CalenderMonthResponse();
        calenderMonthResponse.setDateList(monthList);
        return calenderMonthResponse;
    }
    //공유 일기장은 채팅과 비슷함 작성자 id도 따로 저장해줘야함.그래야 누가 썻는지 암
    //1급 컬렉션
    public List<CalenderDayResponse> getDiaries(Long boardId, String date) {
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

//            Optional<DiaryImg> diaryImg = diaryImgRepository.findFirstByDiaryId(diary.getId(), PageRequest.of(0, 1));
            String diaryImg = diaryImgRepository.findFirstByDiaryIdLimit(diary.getId());
            String thumbUrl = "http://118.67.143.25:8080" + diaryImg;
            calenderDayResponse.setThumbnailUrl(thumbUrl);


            Integer likeCount = likesRepository.countByDiaryId(diary.getId());
            calenderDayResponse.setLikeCount(likeCount);

            Integer commentCount = commentRepository.countByDiaryId(diary.getId());
            calenderDayResponse.setCommentCount(commentCount);

            calenderDayResponse.setSelectedDate(diary.getSelectDate());

            String time = diaryRepository.findDiaryWithTimeAge(diary.getId());
            calenderDayResponse.setTimeStamp(time);

            list.add(calenderDayResponse);

        }

        return list;
    }

    public NewExistResponse newExist(Long memeberId) {


        return null;
    }

    public List<ModeDiaryResponse> getNewDiary(Long boardId) {


        return null;
    }

    public List<ModeDiaryResponse> getAllDiary(Long boardId, Long lastViewId) {


        return null;
    }

    public void setRead(Long boardId) {


    }
}
