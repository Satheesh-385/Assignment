package com.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    private Long authorId;

    private String content;

    private Long parentCommentId;  
    
    private int depthLevel;

    private LocalDateTime createdAt;
}