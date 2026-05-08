package com.project.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long postId;
    private Long authorId;
    private String content;
    private LocalDateTime createdAt;
    private int depthLevel;
    private Long parentCommentId;

    private List<CommentResponse> replies = new ArrayList<>();

    // Default Constructor
    public CommentResponse() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public List<CommentResponse> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentResponse> replies) {
        this.replies = replies;
    }
}