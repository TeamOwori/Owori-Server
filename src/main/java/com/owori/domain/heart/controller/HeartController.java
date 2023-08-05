package com.owori.domain.heart.controller;

import com.owori.domain.heart.dto.request.ToggleHeartRequest;
import com.owori.domain.heart.dto.response.HeartStatusResponse;
import com.owori.domain.story.service.FacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hearts")
public class HeartController {
    private final FacadeService facadeService;

    /**
     * 이야기에 대한 좋아요를 추가 / 삭제하는 컨트롤러입니다.
     * @param request 좋아요 버튼을 누른 이야기 id 값입니다.
     * @return 좋아요가 생성된 상태인 지, 삭제된 상태인지 true / false로 반환합니다.
     */
    @PostMapping
    public ResponseEntity<HeartStatusResponse> toggleHeart(@RequestBody ToggleHeartRequest request) {
        return ResponseEntity.ok(facadeService.toggleHeart(request));
    }
}
