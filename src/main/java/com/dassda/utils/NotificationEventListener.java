package com.dassda.utils;

import com.dassda.entity.*;
import com.dassda.event.*;
import com.dassda.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final BoardRepository boardRepository;
    private final DiaryRepository diaryRepository;
    private final CommentRepository commentRepository;

    private Member member() {
        return GetMember.getCurrentMember();
    }
    private Diary diary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalStateException("일기가 존재하지 않습니다."));
    }
    private void parseJson(Map<String, Object> notificationData, Long memberId) {
        String notificationJson = "";
        try {
            notificationJson = objectMapper.writeValueAsString(notificationData);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(member().getId() != memberId) {
            Long index = redisTemplate.opsForValue().increment("1");
            String key = "notification:" + member().getId() + ":" + memberId + ":" + index;
            redisTemplate.opsForValue().set(key, notificationJson, 30, TimeUnit.DAYS);
        }
    }
    @EventListener
    public void onCommentCreated(CommentCreatedEvent event) {
        Comment comment = event.getComment();
        Diary diary = diary(comment.getDiary().getId());

        if(!diary.getMember().getId().equals(member().getId())) {
            Map<String, Object> notificationData = new HashMap<>() {{
                put("notificationTypeId", 1);
                put("isRead", false);
                put("regDate", LocalDateTime.now());
                put("writerId", comment.getMember().getId());
                put("boardId", diary.getBoard().getId());
                put("boardTitle", diary.getBoard().getTitle());
                put("diaryId", diary.getId());
                put("commentId", comment.getId());
                put("commentContent", comment.getComment());
                put("commentWriterNickname", comment.getMember().getNickname());
            }};
            parseJson(notificationData, diary.getMember().getId());
        }
    }
    @EventListener
    public void onReplyCreated(ReplyCreatedEvent event) {
        Reply reply = event.getReply();
        Optional<Comment> comment = commentRepository.findById(reply.getComment().getId());
        Diary diary = diary(comment.get().getDiary().getId());

        if(!comment.get().getMember().getId().equals(member().getId())) {
            Map<String, Object> notificationData = new HashMap<>() {{
                put("notificationTypeId", 2);
                put("isRead", false);
                put("regDate", LocalDateTime.now());
                put("writerId", reply.getMember().getId());
                put("boardId", diary.getBoard().getId());
                put("boardTitle", diary.getBoard().getTitle());
                put("diaryId", diary.getId());
                put("commentId", reply.getComment().getId());
                put("replyId", reply.getId());
                put("replyContent", reply.getReply());
                put("replyWriterNickname", reply.getMember().getNickname());
            }};
            parseJson(notificationData, reply.getMember().getId());
        }
    }
    @EventListener
    public void onLikeCreated(LikeCreatedEvent event) {
        Likes likes = event.getLikes();
        Diary diary = diary(likes.getDiary().getId());

        if(!likes.getMember().getId().equals(member().getId())) {
            Map<String, Object> notificationData = new HashMap<>() {{
                put("notificationTypeId", 3);
                put("isRead", false);
                put("regDate", LocalDateTime.now());
                put("writerId", likes.getMember().getId());
                put("boardId", diary.getBoard().getId());
                put("boardTitle", diary.getBoard().getTitle());
                put("diaryId", diary.getId());
                put("likeId", likes.getId());
                put("likeMemberNickname", likes.getMember().getNickname());
            }};
            parseJson(notificationData, likes.getMember().getId());
        }
    }
    @EventListener
    public void onDiaryCreated(DiaryCreatedEvent event) {
        Diary diary = event.getDiary();
        Long writerId = diary.getMember().getId();
        if (!diary.getMember().getId().equals(member().getId())) {
            Map<String, Object> notificationData = new HashMap<>() {{
                put("notificationTypeId", 4);
                put("isRead", false);
                put("regDate", LocalDateTime.now());
                put("writerId", diary.getMember().getId());
                put("boardId", diary.getBoard().getId());
                put("boardTitle", diary.getBoard().getTitle());
                put("diaryId", diary.getId());
            }};
            parseJson(notificationData, diary.getMember().getId());
        }
    }
    @EventListener
    public void onShareCreated(NewMemberEvent event) {
        Share share = event.getShare();
        Long boardId = share.getBoard().getId();
        Optional<Board> board = boardRepository.findById(boardId);

        if (!board.get().getMember().getId().equals(member().getId())) {
            Map<String, Object> notificationData = new HashMap<>() {{
                put("notificationTypeId", 5);
                put("isRead", false);
                put("regDate", LocalDateTime.now());
                put("writerId", share.getMember().getId());
                put("boardId", share.getBoard().getId());
                put("boardTitle", share.getBoard().getTitle());
                put("newMemberNickname", share.getMember().getNickname());
            }};
            parseJson(notificationData, share.getMember().getId());
        }

    }
}
