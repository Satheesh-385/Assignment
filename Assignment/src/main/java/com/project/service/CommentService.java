package com.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.dto.CommentRequest;
import com.project.dto.CommentResponse;
import com.project.entity.Comment;
import com.project.enums.InteractionType;
import com.project.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ViralityService viralityService;

    private final BotService botService;

    public CommentService(
            CommentRepository commentRepository,
            ViralityService viralityService,
            BotService botService) {

        this.commentRepository = commentRepository;
        this.viralityService = viralityService;
        this.botService = botService;
    }

    // CREATE COMMENT
    public Comment createComment(CommentRequest request) {

        Comment comment = new Comment();

        comment.setPostId(request.getPostId());

        comment.setAuthorId(request.getAuthorId());

        comment.setContent(request.getContent());

        comment.setCreatedAt(LocalDateTime.now());

        // REPLY COMMENT
        if (request.getParentCommentId() != null) {

            comment.setParentCommentId(
                    request.getParentCommentId());

            comment.setDepthLevel(1);

        } else {

            comment.setDepthLevel(0);
        }

        // SAVE COMMENT
        Comment savedComment = commentRepository.save(comment);

        // UPDATE VIRALITY SCORE
        viralityService.updateViralityScore(
                comment.getPostId(),
                InteractionType.HUMAN_COMMENT
        );

        // CHECK BOT REPLY
        botService.processBotReply(comment.getPostId());

        return savedComment;
    }

    // GET COMMENTS BY POST
    public List<CommentResponse> getCommentsByPost(Long postId) {

        List<Comment> comments =
                commentRepository.findByPostIdOrderByCreatedAt(postId);

        Map<Long, CommentResponse> map = new HashMap<>();

        List<CommentResponse> rootComments = new ArrayList<>();

        // CONVERT ENTITY -> RESPONSE
        for (Comment comment : comments) {

            CommentResponse response = new CommentResponse();

            response.setId(comment.getId());

            response.setPostId(comment.getPostId());

            response.setAuthorId(comment.getAuthorId());

            response.setContent(comment.getContent());

            response.setCreatedAt(comment.getCreatedAt());

            response.setDepthLevel(comment.getDepthLevel());

            response.setParentCommentId(
                    comment.getParentCommentId());

            response.setReplies(new ArrayList<>());

            map.put(comment.getId(), response);
        }

        // BUILD COMMENT TREE
        for (Comment comment : comments) {

            CommentResponse response =
                    map.get(comment.getId());

            if (comment.getParentCommentId() == null) {

                rootComments.add(response);

            } else {

                CommentResponse parent =
                        map.get(comment.getParentCommentId());

                if (parent != null) {

                    parent.getReplies().add(response);
                }
            }
        }

        return rootComments;
    }
}