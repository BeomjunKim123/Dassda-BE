package com.dassda.component;

import com.dassda.entity.*;
import com.dassda.event.CommentCreatedEvent;
import com.dassda.event.DiaryCreatedEvent;
import com.dassda.event.LikeCreatedEvent;
import com.dassda.event.ReplyCreatedEvent;
import com.dassda.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final CommentRepository commentRepository;

    private Member member() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication().getName()
                )
                .orElseThrow(() -> new IllegalStateException("회원 없음"));
    }
    private void parseJson(Map<String, Object> notificationData) {
        String notificationJson = "";
        try {
            notificationJson = objectMapper.writeValueAsString(notificationData);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Long index = redisTemplate.opsForValue().increment("1");
        String key = "notification:" + member().getId() + ":" + index;
        redisTemplate.opsForValue().set(key, notificationJson, 30, TimeUnit.DAYS);
    }
    @EventListener
    public void onCommentCreated(CommentCreatedEvent event) {
        Comment comment = event.getComment();
        Optional<Diary> diary = diaryRepository.findById(comment.getDiary().getId());

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("notificationTypeId", 1);
        notificationData.put("isRead", 0);
        notificationData.put("regDate", LocalDateTime.now());
        notificationData.put("writerId", diary.get().getMember().getId());
        notificationData.put("boardId", diary.get().getBoard().getId());
        notificationData.put("boardTitle", diary.get().getBoard().getTitle());
        notificationData.put("diaryId", diary.get().getId());
        notificationData.put("commentId", comment.getId());
        notificationData.put("commentContent", comment.getComment());
        notificationData.put("commentWriterNickname", comment.getMember().getNickname());

        parseJson(notificationData);
    }
    @EventListener
    public void onReplyCreated(ReplyCreatedEvent event) {
        Reply reply = event.getReply();
        Optional<Comment> comment = commentRepository.findById(reply.getComment().getId());
        Optional<Diary> diary = diaryRepository.findById(comment.get().getDiary().getId());

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("notificationTypeId", 2);
        notificationData.put("isRead", 0);
        notificationData.put("regDate", LocalDateTime.now());
        notificationData.put("writerId", diary.get().getMember().getId());
        notificationData.put("boardId", diary.get().getBoard().getId());
        notificationData.put("boardTitle", diary.get().getBoard().getTitle());
        notificationData.put("diaryId", diary.get().getId());
        notificationData.put("commentId", reply.getComment().getId());
        notificationData.put("replyId", reply.getId());
        notificationData.put("replyContent", reply.getReply());
        notificationData.put("replyWriterNickname", reply.getMember().getNickname());

        parseJson(notificationData);
    }
    @EventListener
    public void onLikeCreated(LikeCreatedEvent event) {
        Likes likes = event.getLikes();
        Optional<Diary> diary = diaryRepository.findById(likes.getDiary().getId());

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("notificationTypeId", 3);
        notificationData.put("isRead", 0);
        notificationData.put("regDate", LocalDateTime.now());
        notificationData.put("writerId", diary.get().getMember().getId());
        notificationData.put("boardId", diary.get().getBoard().getId());
        notificationData.put("boardTitle", diary.get().getBoard().getTitle());
        notificationData.put("diaryId", diary.get().getId());
        notificationData.put("likeId", likes.getId());
        notificationData.put("likeMemberNickname", likes.getMember().getNickname());

        parseJson(notificationData);
    }
    @EventListener
    public void onDiaryCreated(DiaryCreatedEvent event) {
        Diary diary = event.getDiary();

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("notificationTypeId", 4);
        notificationData.put("isRead", 0);
        notificationData.put("regDate", LocalDateTime.now());
        notificationData.put("writerId", diary.getMember().getId());
        notificationData.put("boardId", diary.getBoard().getId());
        notificationData.put("boardTitle", diary.getBoard().getTitle());
        notificationData.put("diaryId", diary.getId());

        parseJson(notificationData);
    }

}
