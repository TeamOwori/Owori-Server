package com.owori.domain.story.controller;

import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.request.UpdateStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryGroupResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.dto.response.StoryIdResponse;
import com.owori.domain.story.dto.response.StoryPagingResponse;
import com.owori.domain.story.service.FacadeService;
import com.owori.domain.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;
    private final FacadeService facadeService;

    /**
     * todo : 다음 배포 시 삭제
     * 이야기를 전체 조회를 위한 컨트롤러입니다.
     * @param sort 정렬 조건입니다.
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping("/find")
    public ResponseEntity<FindAllStoryGroupResponse> findAllStory2(@RequestParam(value = "sort") String sort) {
        return ResponseEntity.ok(storyService.findAllStory2(sort));
    }

    // * todo : 다음 배포 시 삭제
    @GetMapping("/search/temp")
    public ResponseEntity<FindAllStoryGroupResponse> findStoryBySearch2(
            @RequestParam @Size(min = 2, message = "검색어를 2글자 이상 입력해주세요.") String keyword,
            @RequestParam(value = "sort") String sort) {
        return ResponseEntity.ok(storyService.findStoryBySearch2(keyword, sort));
    }

    /**
     * todo: 다음 배포 시 삭제
     * 유저가 작성한 이야기 조회를 위한 임시 컨트롤러입니다.
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping("/member/find")
    public ResponseEntity<FindAllStoryGroupResponse> findStoryByWriter2() {
        return ResponseEntity.ok(storyService.findStoryByWriter2());
    }

    /**
     * todo: 다음 배포 시 삭제
     * 유저가 작성한 좋아한 조회를 위한 임시 컨트롤러입니다.
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping("/heart/find")
    public ResponseEntity<FindAllStoryGroupResponse> findStoryByHeart2() {
        return ResponseEntity.ok(storyService.findStoryByHeart2());
    }

    /**
     * 이야기를 생성합니다.
     * @param request 이야기 생성을 위한 dto 입니다.
     * @return 생성된 이야기의 id가 반환됩니다.
     */
    @PostMapping
    public ResponseEntity<StoryIdResponse> addStory(@RequestBody @Valid PostStoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.addStory(request));
    }

    /**
     * 페이징을 사용한 이야기를 전체 조회를 위한 컨트롤러입니다.
     * @param pageable
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping
    public ResponseEntity<StoryPagingResponse> findAllStory(@PageableDefault(sort = "created_at", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(storyService.findAllStory(pageable));
    }

    /**
     * 이야기 검색을 위한 컨트롤러입니다.
     * @param keyword 검색어입니다.
     * @param pageable
     * @return 검색 결과 dto가 반환됩니다.
     */
    @GetMapping("/search")
    public ResponseEntity<StoryPagingResponse> findStoryBySearch(
            @RequestParam @Size(min = 2, message = "검색어를 2글자 이상 입력해주세요.") String keyword,
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(storyService.findStoryBySearch(keyword, pageable));
    }

    /**
     * 유저가 작성한 이야기 조회를 위한 컨트롤러입니다.
     * @param pageable
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping("/member")
    public ResponseEntity<StoryPagingResponse> findStoryByWriter(@PageableDefault(sort = "created_at", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(storyService.findStoryByWriter(pageable));
    }

    /**
     * 유저가 작성한 좋아한 조회를 위한 컨트롤러입니다.
     * @param pageable
     * @return 전체 조회 dto가 반환됩니다.
     */
    @GetMapping("/heart")
    public ResponseEntity<StoryPagingResponse> findStoryByHeart(
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable) {

        return ResponseEntity.ok(storyService.findStoryByHeart(pageable));
    }

    /**
     * 이야기를 상세 조회를 위한 컨트롤러입니다.
     * @param storyId 조회할 story의 id 값 입니다.
     * @return 상세 조회 dto가 반환됩니다.
     */
    @GetMapping("/{storyId}")
    public ResponseEntity<FindStoryResponse> findStory(@PathVariable UUID storyId) {
        return ResponseEntity.ok(facadeService.findStory(storyId));
    }

    /**
     * 이야기 수정을 위한 컨트롤러입니다.
     * @param request 수정 사항이 담긴 dto 입니다.
     * @return 수정한 story의 id 값이 반환됩니다.
     */
    @PostMapping("/update")
    public ResponseEntity<StoryIdResponse> updateStory(@RequestBody @Valid UpdateStoryRequest request) {
        return ResponseEntity.ok(storyService.updateStory(request));
    }

    /**
     * 이야기 삭제를 위한 컨트롤러입니다.
     * @param storyId 수정할 story의 id 값 입니다.
     */
    @DeleteMapping("/{storyId}")
    public ResponseEntity<Void> removeStory(@PathVariable UUID storyId) {
        facadeService.removeStory(storyId);
        return ResponseEntity.ok().build();
    }
}
