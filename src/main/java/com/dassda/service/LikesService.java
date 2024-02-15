package com.dassda.service;



import com.dassda.entity.Diary;
import com.dassda.entity.Likes;
import com.dassda.entity.Member;
import com.dassda.event.DiaryCreatedEvent;
import com.dassda.event.LikeCreatedEvent;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.LikesRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.response.LikesResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final LikesRepository likesRepository;
    private Member member() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(
                        () -> new IllegalStateException("멤버 없다")
                );
    }
    public void toggleLike(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기가 존재하지 않습니다."));

        Long memberId = member().getId();

//        boolean existsedLike = likesRepository.existsMemberIdAndDiaryId(memberId, diaryId);
        Optional<Likes> likesOptional = likesRepository.findByMemberIdAndDiaryId(memberId, diaryId);

        if (!likesOptional.isPresent()) {
            Likes like = new Likes();
            like.setMember(member());
            like.setDiary(diary);
            likesRepository.save(like);
        } else {
            likesRepository.delete(likesOptional.get());
        }
    }
    @Transactional
    public List<LikesResponse> getLikesForDiary(Long diaryId) {
        List<Likes> likesList = likesRepository.findActiveLikesByDiaryId(diaryId);
        //int likeCount = likesRepository.countByDiaryId(diaryId);
        Long currentUserId = member().getId();

        return likesList.stream()
                .map(like -> {
                    Member member = like.getMember();
                    LikesResponse response = new LikesResponse();
                    response.setNickname(member.getNickname());
                    response.setProfileUrl(member.getProfile_image_url());
                    response.setLikeCount(likesRepository.countByDiaryId(like.getDiary().getId()));
                    // 현재 사용자가 좋아요를 했는지 여부 설정
                    response.setLikedByCurrentUser(likesRepository.existsMemberIdAndDiaryId(currentUserId, diaryId));
                    System.out.println(member.getNickname());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
