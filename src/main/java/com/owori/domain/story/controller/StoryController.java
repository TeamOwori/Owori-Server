package com.owori.domain.story.controller;

import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.AddStoryResponse;
import com.owori.domain.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<AddStoryResponse> saveStory(@RequestPart(required = false) AddStoryRequest request,
                                                      @RequestPart(required = false) List<MultipartFile> images){

        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.saveStory(request, images));
    }


}
