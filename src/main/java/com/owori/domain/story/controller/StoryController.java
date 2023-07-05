package com.owori.domain.story.controller;

import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.AddStoryResponse;
import com.owori.domain.story.dto.response.findAlbumStoryResponse;
import com.owori.domain.story.dto.response.findListStoryResponse;
import com.owori.domain.story.dto.response.findStoryResponse;
import com.owori.domain.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<AddStoryResponse> addStory(@RequestPart(required = false) AddStoryRequest request,
                                                      @RequestPart(required = false) List<MultipartFile> images){

        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.addStory(request, images));
    }

    @GetMapping("/album")
    public ResponseEntity<List<findAlbumStoryResponse>> findAlbumStory(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                 @RequestParam Long lastId){

        return ResponseEntity.ok(storyService.findAlbumStory(pageable, lastId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<findListStoryResponse>> findListStory(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                     @RequestParam Long lastId){

        return ResponseEntity.ok(storyService.findListStory(pageable, lastId));
    }

    @GetMapping("/{storyId}")
    public ResponseEntity<findStoryResponse> findStory(@PathVariable Long storyId){

        return ResponseEntity.ok(storyService.findStory(storyId));
    }

}
