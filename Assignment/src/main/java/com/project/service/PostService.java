package com.project.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.entity.Post;
import com.project.enums.InteractionType;
import com.project.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final ViralityService viralityService;

    private final BotService botService;

    public PostService(
            PostRepository postRepository,
            ViralityService viralityService,
            BotService botService) {

        this.postRepository = postRepository;
        this.viralityService = viralityService;
        this.botService = botService;
    }

    // CREATE POST
    public Post createPost(Post post) {

        post.setCreatedAt(LocalDateTime.now());

        post.setLikeCount(0);

        return postRepository.save(post);
    }

    // LIKE POST
    public Post likePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // INCREMENT LIKE COUNT
        post.setLikeCount(post.getLikeCount() + 1);

        // UPDATE VIRALITY SCORE IN REDIS
        viralityService.updateViralityScore(
                postId,
                InteractionType.HUMAN_LIKE
        );

        // CHECK BOT REPLY
        botService.processBotReply(postId);

        return postRepository.save(post);
    }

    // GET POST
    public Post getPost(Long postId) {

        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));
    }
}