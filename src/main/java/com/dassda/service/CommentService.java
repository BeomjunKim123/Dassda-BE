package com.dassda.service;

import com.dassda.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    public void addComment(Long diaryId) {
    }

    public void updateComment(Long diaryId, Long commentId) {
    }

    public CommentResponse getComment(Long diaryId, int pageSize, int lastViewId) {
        return null;
    }

    public void deleteComment(Long diaryId, Long commentId) {
    }
}
