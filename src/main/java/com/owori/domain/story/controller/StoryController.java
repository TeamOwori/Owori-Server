package com.owori.domain.story.controller;

import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryGroupResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.service.FacadeService;
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
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;
    private final FacadeService facadeService;

    /**
     * 이야기를 생성합니다.
     * @param request 이야기 생성을 위한 dto 입니다.
     * @return 생성된 이야기의 id가 반환됩니다.
     */
    @PostMapping
    public ResponseEntity<IdResponse<UUID>> addStory(@RequestBody PostStoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.addStory(request));
    }

    /**
     * 이야기를 전체 조회를 위한 컨트롤러입니다.
     * @param pageable
     * @param lastViewed 조회할 게시글의 기준 (year_month) 입니다.
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping
    public ResponseEntity<FindAllStoryGroupResponse> findAllStory(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate lastViewed){

        return ResponseEntity.ok(storyService.findAllStory(pageable, lastViewed));
    }

    /**
     * 이야기 검색을 위한 컨트롤러입니다.
     * @param keyword 검색어입니다.
     * @param pageable
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping( "/search")
    public ResponseEntity<FindAllStoryGroupResponse> findStoryBySearch(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                       @RequestParam String keyword,
                                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate lastViewed){

        return ResponseEntity.ok(storyService.findStoryBySearch(keyword, pageable, lastViewed));
    }

    /**
     * 이야기를 상세 조회를 위한 컨트롤러입니다.
     * @param storyId 조회할 story의 id 값 입니다.
     * @return 상세 조회 dto가 반환됩니다.
     */
    @GetMapping("/{storyId}")
    public ResponseEntity<FindStoryResponse> findStory(@PathVariable UUID storyId){
        return ResponseEntity.ok(facadeService.findStory(storyId));
    }

    /**
     * 이야기 수정을 위한 컨트롤러입니다.
     * @param storyId 수정할 story의 id 값 입니다.
     * @param request 수정 사항이 담긴 dto 입니다.
     * @return 수정한 story의 id 값이 반환됩니다.
     */
    @PostMapping("/{storyId}")
    public ResponseEntity<IdResponse<UUID>> updateStory(@PathVariable UUID storyId, @RequestBody PostStoryRequest request){
        return ResponseEntity.ok(storyService.updateStory(storyId, request));
    }

    /**
     * 이야기 삭제를 위한 컨트롤러입니다.
     * @param storyId 수정할 story의 id 값 입니다.
     */
    @DeleteMapping("/{storyId}")
    public ResponseEntity<Void> removeStory(@PathVariable UUID storyId){
        facadeService.removeStory(storyId);
        return ResponseEntity.ok().build();
    }

}
