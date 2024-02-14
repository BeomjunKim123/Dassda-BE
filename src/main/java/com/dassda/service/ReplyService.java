package com.dassda.service;

import com.dassda.entity.Comment;
import com.dassda.entity.Member;
import com.dassda.entity.Reply;
import com.dassda.event.DiaryCreatedEvent;
import com.dassda.event.ReplyCreatedEvent;
import com.dassda.repository.CommentRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ReplyRepository;
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
public class ReplyService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final ApplicationEventPublisher eventPublisher;

    private Member member() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(() -> new IllegalStateException("존재하지 않은 멤버"));
    }


    public void addReply(Long commentId, CommentOrReplyRequest commentOrReplyRequest) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Reply reply = new Reply();
        reply.setMember(member());
        reply.setComment(commentOptional.get());
        reply.setReply(commentOrReplyRequest.getContents());
        reply.setRegDate(LocalDateTime.now());
        reply.setUpdateDate(LocalDateTime.now());
        reply.setBackUp(false);
        replyRepository.save(reply);
        eventPublisher.publishEvent(new ReplyCreatedEvent(this, reply));
    }

    public void updateReply(Long commentId, Long replyId, CommentOrReplyRequest commentOrReplyRequest) {
        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        Reply reply = replyOptional.get();
        reply.setId(reply.getId());
        reply.setReply(commentOrReplyRequest.getContents());
        reply.setUpdateDate(LocalDateTime.now());
        replyRepository.save(reply);
    }

    public void deleteReply(Long commentId, Long replyId) {
        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if(replyOptional.isPresent()) {
            Reply reply = replyOptional.get();
            reply.setBackUp(true);
            replyRepository.save(reply);
        } else {
            throw new IllegalStateException("삭제된 답글입니다.");
        }
    }

    public List<CommentOrReplyResponse> getReply(Long commentId, int lastViewId) {
        List<CommentOrReplyResponse> commentOrReplyResponseList = new ArrayList<>();
        List<Reply> replyList;

        if(lastViewId == 0) {
            replyList = replyRepository.findByCommentIdThree(commentId);
        } else {
            replyList = replyRepository.findByCommentIdExceptThree(commentId);
        }

        for(Reply reply : replyList) {
            CommentOrReplyResponse commentOrReplyResponse = new CommentOrReplyResponse();
            commentOrReplyResponse.setId(reply.getId());
            commentOrReplyResponse.setNickname(reply.getMember().getNickname());
            commentOrReplyResponse.setProfilUrl(reply.getMember().getProfile_image_url());
            commentOrReplyResponse.setContents(reply.getReply());
            commentOrReplyResponse.setRegDate(LocalDateTime.now());

            if(member().getId() == reply.getMember().getId()) {
                commentOrReplyResponse.setOwned(true);
            } else {
                commentOrReplyResponse.setOwned(false);
            }

            if(reply.isBackUp()) {
                commentOrReplyResponse.setDeletedMark(true);
            } else {
                commentOrReplyResponse.setDeletedMark(false);
            }
            commentOrReplyResponseList.add(commentOrReplyResponse);
        }
        return commentOrReplyResponseList;
    }
}
