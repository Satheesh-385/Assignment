package com.project.controller;

import com.project.dto.CommentRequest;
import com.project.dto.CommentResponse;
import com.project.entity.Comment;
import com.project.service.CommentService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ADD COMMENT
    @PostMapping("/{postId}/comments")
    public Comment addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {

        request.setPostId(postId);

        return commentService.createComment(request);
    }

    // GET COMMENTS BY POST
    @GetMapping("/{postId}/comments")
    public List<CommentResponse> getComments(
            @PathVariable Long postId) {

        return commentService.getCommentsByPost(postId);
    }
}