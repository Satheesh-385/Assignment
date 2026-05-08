package com.project.controller;

import com.project.entity.Comment;
import com.project.service.BotService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    private final BotService botService;

    public BotController(BotService botService) {
        this.botService = botService;
    }

    @PostMapping("/reply/{postId}")
    public Comment botReply(
            @PathVariable Long postId) {

        return botService.processBotReply(postId);
    }
}