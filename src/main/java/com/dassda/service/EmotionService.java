package com.dassda.service;

import com.dassda.entity.Diary;
import com.dassda.entity.Member;
import com.dassda.repository.DiaryRepository;
import com.dassda.response.EmotionResponse;
import com.dassda.utils.GetMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final DiaryRepository diaryRepository;

    private Member member() {
        return GetMember.getCurrentMember();
    }

    public EmotionResponse GetEmotion() {
        Long memberId = member().getId();
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        LocalDate today = LocalDate.now();
//        List<Diary> entries = diaryRepository.findByUserAndDateBetween(memberId, oneMonthAgo, today);

        Map<DayOfWeek, Map<String, Long>> weeklyMoodCount = new HashMap<>();
//        for (Diary entry : entries) {
//            DayOfWeek dayOfWeek = entry.getSelectDate().getDayOfWeek();
//            String mood = entry.getSticker().getSticker();
//            weeklyMoodCount.computeIfAbsent(dayOfWeek, k -> new HashMap<>()).merge(mood, 1L, Long::sum);
//        }

        DayOfWeek tomorrow = today.plusDays(1).getDayOfWeek();
        Map<String, Long> tomorrowMoodCounts = weeklyMoodCount.getOrDefault(tomorrow, new HashMap<>());

        String predictedMood = null;
        long maxCount = 0;
        for (Map.Entry<String, Long> moodCount : tomorrowMoodCounts.entrySet()) {
            if (moodCount.getValue() > maxCount) {
                maxCount = moodCount.getValue();
                predictedMood = moodCount.getKey();
            }
        }
        double totalMoods = tomorrowMoodCounts.values().stream().mapToLong(Long::longValue).sum();
        double probability = maxCount / totalMoods;

        return null;
    }
}
