package com.owori.domain.image.mapper;

import com.owori.domain.image.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public Image toEntity(String imgUrl, Long order){
        return Image.builder()
                .url(imgUrl)
                .orderNum(order)
                .build();
    }
}
