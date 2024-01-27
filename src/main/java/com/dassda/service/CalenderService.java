package com.dassda.service;

import com.dassda.entity.Diary;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.CommentRepository;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.LikesRepository;
import com.dassda.response.CalenderDayResponse;
import com.dassda.response.CalenderMonthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final DiaryRepository diaryRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    public CalenderMonthResponse getMonth(String date) {
        String month = date.substring(0, 7);
        List<String> monthList = diaryRepository.findDiaryDatesByMonth(month);
        CalenderMonthResponse calenderMonthResponse = new CalenderMonthResponse();
        calenderMonthResponse.setDiaryDate(monthList);
        return calenderMonthResponse;
    }
    //공유 일기장은 채팅과 비슷함 작성자 id도 따로 저장해줘야함.그래야 누가 썻는지 암
    //1급 컬렉션
    public List<CalenderDayResponse> getDiaries(Long boardId, String date) {
        date = date.replace("\"", "");
        LocalDate day = LocalDate.parse(date);
        List<CalenderDayResponse> list = new ArrayList<>();
        List<Diary> diaryList = diaryRepository.findByBoardIdAndDay(boardId, day);
        for(int i=0; i<diaryList.size(); i++) {
            CalenderDayResponse calenderDayResponse = new CalenderDayResponse();
            calenderDayResponse.setId(diaryList.get(i).getId());
            calenderDayResponse.setWrite(diaryList.get(i).getWrites());
            calenderDayResponse.setSticker(diaryList.get(i).getSticker());
            calenderDayResponse.setContents(diaryList.get(i).getDiaryContent());
            calenderDayResponse.setBoard(diaryList.get(i).getBoard());
            calenderDayResponse.setBackUp(diaryList.get(i).isBackUp());
            calenderDayResponse.setRegDate(diaryList.get(i).getRegDate());
            calenderDayResponse.setUpdateDate(diaryList.get(i).getUpdateDate());
            Long likesCount = likesRepository.countByDiaryId(diaryList.get(i).getBoard().getId());
            calenderDayResponse.setLikesCount(likesCount);
            Long commentCount = commentRepository.countByDiaryId(diaryList.get(i).getBoard().getId());
            calenderDayResponse.setCommentCount(commentCount);
            list.add(calenderDayResponse);
        }
        return list;
    }
}
