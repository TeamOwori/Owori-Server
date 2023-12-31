package com.owori.domain.image.controller;

import com.owori.domain.image.dto.response.ImagesStoryResponse;
import com.owori.domain.image.service.ImageService;
import com.owori.global.dto.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    /**
     * 이야기에 첨부될 이미지를 저장합니다.
     * @param storyImages 이야기에 첨부한 사진 입니다. 10장까지 첨부 가능합니다.
     * @return 생성된 이야기의 id가 반환됩니다.
     */
    @PostMapping
    public ResponseEntity<ImagesStoryResponse> addStoryImage(@RequestPart(value = "story_images", required = false) List<MultipartFile> storyImages) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.addStoryImage(storyImages));
    }
}
