package com.owori.domain.story.controller;

import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAlbumStoryGroupResponse;
import com.owori.domain.story.dto.response.FindListStoryGroupResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.service.StoryService;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;

    /**
     * 이야기를 생성합니다.
     * @param request 이야기 생성을 위한 dto 입니다.
     * @return 생성된 이야기의 id가 반환됩니다.
     */
    @PostMapping
    public ResponseEntity<IdResponse<Long>> addStory(@RequestBody AddStoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.addStory(request));
    }

    /**
     * 이야기를 앨범형으로 조회합니다.
     * @param pageable
     * @param lastViewed 조회할 게시글의 기준 (year_month) 입니다.
     * @return 앨범형 조회 dto가 반환됩니다.
     */
    @GetMapping("/album")
    public ResponseEntity<FindAlbumStoryGroupResponse> findAlbumStory(@PageableDefault(sort = "createAt", direction = DESC) Pageable pageable,
                                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate lastViewed){

        return ResponseEntity.ok(storyService.findAlbumStory(pageable, lastViewed));
    }

    @GetMapping("/list")
    public ResponseEntity<FindListStoryGroupResponse> findListStory(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                    @RequestParam Long lastId){

        return ResponseEntity.ok(storyService.findListStory(pageable, lastId));
    }

    @GetMapping("/{storyId}")
    public ResponseEntity<FindStoryResponse> findStory(@PathVariable Long storyId){

        return ResponseEntity.ok(storyService.findStory(storyId));
    }

}
