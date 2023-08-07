package com.owori.domain.image.dto.response;

import com.owori.global.dto.ImageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImagesStoryResponse {
    List<String> story_images;
}
