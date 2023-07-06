package com.owori.domain.image.service;

import com.owori.domain.image.entity.Image;
import com.owori.domain.image.mapper.ImageMapper;
import com.owori.domain.image.repository.ImageRepository;
import com.owori.domain.image.exception.ImageLimitExceededException;
import com.owori.domain.story.entity.Story;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import com.owori.utils.ImageUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageService implements EntityLoader<Image, UUID> {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageUploadUtil imageUploadUtil;

    @Transactional
    public List<UUID> addStoryImage(List<MultipartFile> images) throws IOException {
        if (images.size() > 10) {
            throw new ImageLimitExceededException();
        }

        List<UUID> response = new ArrayList<>();
        Long order = 1L;

        for (MultipartFile image : images) {
            if(image != null) {
                String imgUrl = imageUploadUtil.uploadImage("story", image);
                Image newImage = imageMapper.toEntity(imgUrl, order);
                imageRepository.save(newImage);
                response.add(newImage.getId());
                order += 1;
            }
        }
        return response;
    }

    @Transactional
    public void updateStory(Story newStory, List<UUID> images) {
        for (UUID imageID : images){
            loadEntity(imageID).updateStory(newStory);
        }
    }

    @Override
    public Image loadEntity(UUID id) {
        return imageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


}
