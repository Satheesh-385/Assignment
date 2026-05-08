package com.project.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.entity.Comment;
import com.project.entity.Post;
import com.project.enums.InteractionType;
import com.project.repository.CommentRepository;
import com.project.repository.PostRepository;

@Service
public class BotService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ViralityService viralityService;
    private final BotGuardService botGuardService;
    private final NotificationService notificationService;

    public BotService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            ViralityService viralityService,
            BotGuardService botGuardService,
            NotificationService notificationService) {

        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.viralityService = viralityService;
        this.botGuardService = botGuardService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Comment processBotReply(Long postId) {

        // CHECK POST
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // DEMO VALUES
        Long botId = 1L;
        Long humanId = post.getAuthorId();

        // DEPTH LEVEL
        int depthLevel = 1;

        // =========================
        // ATOMIC LOCK CHECKS
        // =========================

        // HORIZONTAL CAP
        botGuardService.checkHorizontalCap(postId);

        // VERTICAL CAP
        botGuardService.checkVerticalCap(depthLevel);

        // COOLDOWN CAP
        botGuardService.checkCooldown(
                botId,
                humanId
        );

        // =========================
        // CREATE BOT COMMENT
        // =========================

        Comment comment = new Comment();

        comment.setPostId(postId);

        comment.setAuthorId(botId);

        comment.setContent(
                "Hello! I am an AI Bot Reply.");

        comment.setDepthLevel(depthLevel);

        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment =
                commentRepository.save(comment);
        
        notificationService.handleBotNotification(
                humanId,
                "Bot replied to your post"
        );

        // UPDATE VIRALITY SCORE
        viralityService.updateViralityScore(
                postId,
                InteractionType.BOT_REPLY
        );

        return savedComment;
    }
}