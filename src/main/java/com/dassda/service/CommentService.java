package com.dassda.service;

import com.dassda.entity.Comment;
import com.dassda.entity.Member;
import com.dassda.repository.MemberRepository;
import com.dassda.request.CommentRequest;
import com.dassda.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private MemberRepository memberRepository;

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
    public void addComment(Long diaryId, CommentRequest commentRequest) {
        Comment comment = new Comment();


    }

    public void updateComment(Long diaryId, Long commentId) {
    }

    public CommentResponse getComment(Long diaryId, int pageSize, int lastViewId) {
        return null;
    }

    public void deleteComment(Long diaryId, Long commentId) {
    }
}
