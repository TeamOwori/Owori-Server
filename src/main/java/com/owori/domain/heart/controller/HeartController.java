package com.owori.domain.heart.controller;

import com.owori.domain.heart.dto.HeartStatusResponse;
import com.owori.domain.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hearts")
public class HeartController {
    private final HeartService heartService;

    /**
     * 이야기에 대한 좋아요를 추가 / 삭제하는 컨트롤러입니다.
     * @param storyId 좋아요 버튼을 누른 이야기 id 값입니다.
     * @return 좋아요가 생성된 상태인 지, 삭제된 상태인지 true / false로 반환합니다.
     */
    @PostMapping("/{storyId}")
    public ResponseEntity<HeartStatusResponse> toggleHeart(@PathVariable UUID storyId){
        return ResponseEntity.ok(heartService.toggleHeart(storyId));
    }

}
