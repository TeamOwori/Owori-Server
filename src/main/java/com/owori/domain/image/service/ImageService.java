package com.owori.domain.image.service;

import com.owori.domain.image.entity.Image;
import com.owori.domain.image.exception.ImageLimitExceededException;
import com.owori.domain.image.mapper.ImageMapper;
import com.owori.domain.image.repository.ImageRepository;
import com.owori.domain.story.entity.Story;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import com.owori.utils.S3ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ImageService implements EntityLoader<Image, UUID> {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final S3ImageComponent s3ImageComponent;

    public List<UUID> addStoryImage(List<MultipartFile> images) throws IOException {
        if (images.size() > 10) {
            throw new ImageLimitExceededException();
        }

        List<UUID> response = new ArrayList<>();
        Long order = 1L;
        for (MultipartFile image : images) {
            if(image != null) {
                String imgUrl = s3ImageComponent.uploadImage("story", image);
                Image newImage = imageMapper.toEntity(imgUrl, order);
                imageRepository.save(newImage);
                response.add(newImage.getId());
                order += 1;
            }
        }
        return response;
    }

    @Transactional
    public void updateStory(Story story, List<UUID> imageIds) {
        removeImages(story);
        imageIds.stream()
                .map(this::loadEntity)
                .sorted(Comparator.comparing(Image::getOrderNum))
                .forEach(image -> image.updateStory(story));
    }

    @Transactional
    public void removeImages(Story story){
        List<Image> oldImages = imageRepository.findAllByStory(story);

        Optional.ofNullable(oldImages).ifPresent(images -> {
            images.forEach(img -> {
                s3ImageComponent.deleteImage(img.getUrl());
                story.removeImage(img);
            });
        });
    }

    @Override
    public Image loadEntity(UUID id) {
        return imageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


}
