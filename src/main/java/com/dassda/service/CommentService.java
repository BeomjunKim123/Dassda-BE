package com.dassda.service;

import com.dassda.entity.Comment;
import com.dassda.entity.Diary;
import com.dassda.entity.Member;
import com.dassda.event.CommentCreatedEvent;
import com.dassda.event.DiaryCreatedEvent;
import com.dassda.repository.CommentRepository;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.request.CommentOrReplyRequest;
import com.dassda.response.CommentOrReplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    private Member member() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(() -> new IllegalStateException("존재하지 않음"));
    }
    public void addComment(Long diaryId, CommentOrReplyRequest commentOrReplyRequest) {
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        Comment comment = new Comment();
        comment.setMember(member());
        comment.setDiary(diaryOptional.get());
        comment.setComment(commentOrReplyRequest.getContents());
        comment.setRegDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());
        comment.setBackUp(false);
        commentRepository.save(comment);
        eventPublisher.publishEvent(new CommentCreatedEvent(this, comment));
    }
    public void updateComment(Long diaryId, Long commentId, CommentOrReplyRequest commentOrReplyRequest) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Comment comment = commentOptional.get();
        comment.setId(commentOptional.get().getId());
        comment.setComment(commentOrReplyRequest.getContents());
        comment.setUpdateDate(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public List<CommentOrReplyResponse> getComment(Long diaryId, int pageSize, int lastViewId) {
        List<CommentOrReplyResponse> commentOrReplyResponseList = new ArrayList<>();
        List<Comment> commentList = commentRepository.findByDiaryId(diaryId);

        int startIndex = Math.max(lastViewId, 0);
        int endIndex = Math.min(startIndex + pageSize, commentList.size());
        for (int i = startIndex; i < endIndex; i++) {
            CommentOrReplyResponse commentOrReplyResponse = new CommentOrReplyResponse();
            Comment comment = commentList.get(i);
            commentOrReplyResponse.setId(comment.getId());
            commentOrReplyResponse.setNickname(comment.getMember().getNickname());
            commentOrReplyResponse.setProfilUrl(comment.getMember().getProfile_image_url());
            commentOrReplyResponse.setContents(comment.getComment());
            commentOrReplyResponse.setRegDate(comment.getRegDate());

            if (member().getId() == comment.getMember().getId()) {
                commentOrReplyResponse.setOwned(true);
            } else {
                commentOrReplyResponse.setOwned(false);
            }

            if (comment.isBackUp()) {
                commentOrReplyResponse.setDeletedMark(true);
            } else {
                commentOrReplyResponse.setDeletedMark(false);
            }
            commentOrReplyResponseList.add(commentOrReplyResponse);
        }
        return commentOrReplyResponseList;
    }

    public void deleteComment(Long diaryId, Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setBackUp(true);
            commentRepository.save(comment);
        } else {
            throw new IllegalStateException("삭제된 댓글입니다.");
        }
    }
}
