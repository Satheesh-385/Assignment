package com.project.controller;

import com.project.service.ViralityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/virality")
public class ViralityController {

    private final ViralityService viralityService;

    public ViralityController(ViralityService viralityService) {
        this.viralityService = viralityService;
    }

    @GetMapping("/{postId}")
    public Long getScore(@PathVariable Long postId) {

        return viralityService.getViralityScore(postId);
    }
}