package com.project.controller;

import com.project.entity.Post;
import com.project.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // ✅ Create Post
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    // ✅ Like Post
    @PostMapping("/{postId}/like")
    public Post likePost(@PathVariable Long postId) {
        return postService.likePost(postId);
    }
}