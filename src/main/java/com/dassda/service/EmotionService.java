package com.dassda.service;

import com.dassda.entity.Diary;
import com.dassda.entity.Member;
import com.dassda.entity.Sticker;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.StickerRepository;
import com.dassda.response.EmotionResponse;
import com.dassda.utils.GetMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final DiaryRepository diaryRepository;
    private final StickerRepository stickerRepository;

    private Member member() {
        return GetMember.getCurrentMember();
    }

    public EmotionResponse GetEmotion() {
//        Long memberId = member().getId();
//        List<Diary> diaries = diaryRepository.findByMemberId(memberId);
//
//        if(diaries.isEmpty() || ChronoUnit.DAYS.between(diaries.get(0).getSelectDate(), LocalDate.now()) < 30) {
//            return getRandomResponse();
//        }
//
//        Map<DayOfWeek, Map<String, Long>> moodStatus = new HashMap<>();
//        for(Diary diary : diaries) {
//            DayOfWeek dayOfWeek = diary.getSelectDate().getDayOfWeek();
//            Long mood = diary.getSticker().getId();
//            moodStatus.computeIfAbsent(dayOfWeek, k -> new HashMap<>()).merge(mood, 1L, Long::sum);
//        }
//
//        DayOfWeek todayDayOfWeek = LocalDate.now().getDayOfWeek();
//        if(todayDayOfWeek.equals(DayOfWeek.MONDAY)) {
//            return null;
//        } else {
//            return getRandomResponse();
//        }
        return null;
    }

    public EmotionResponse getRandomResponse() {
        List<Sticker> stickers = stickerRepository.findAll();
        Sticker randomSticker = stickers.get(new Random().nextInt(stickers.size()));
        return new EmotionResponse(randomSticker.getQuestion());
    }

//    private EmotionResponse getPrediction(Map<DayOfWeek, Map<String, Long>> moodStatus, DayOfWeek todayDayOfWeek) {
//        Map<String, Long> todayMood = moodStatus.getOrDefault(todayDayOfWeek, new HashMap<>());
//
//        String predictedMood = todayMood.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse(null);
//
//        if(predictedMood != null) {
//            List<Sticker> stickers = stickerRepository.findBySticker(predictedMood);
//            if(!stickers.isEmpty()) {
//                String question = stickers.get(new Random().nextInt(stickers.size())).getQuestion();
//                return new EmotionResponse(question);
//            }
//        }
//        return getRandomResponse();
//
//    }
}
