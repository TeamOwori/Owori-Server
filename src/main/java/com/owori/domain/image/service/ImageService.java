package com.owori.domain.image.service;

import com.owori.domain.image.entity.Image;
import com.owori.domain.image.exception.ImageLimitExceededException;
import com.owori.domain.image.mapper.ImageMapper;
import com.owori.domain.image.repository.ImageRepository;
import com.owori.domain.story.entity.Story;
import com.owori.global.dto.ImageResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import com.owori.utils.S3ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
public class ImageService implements EntityLoader<Image, UUID> {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final S3ImageComponent s3ImageComponent;

    public List<ImageResponse> addStoryImage(List<MultipartFile> images) {
        if (images.size() > 10) {
            throw new ImageLimitExceededException();
        }

        images.removeIf(Objects::isNull);
        List<String> urls = IntStream.range(0, images.size()).mapToObj(i -> uploadImage(images.get(i), i)).toList();
        return urls.stream().map(ImageResponse::new).toList();
    }

    private String uploadImage(MultipartFile file, long order) {
        String imgUrl = s3ImageComponent.uploadImage("story", file);
        Image newImage = imageRepository.save(imageMapper.toEntity(imgUrl, order));
        return newImage.getUrl();
    }

    @Transactional
    public void updateStory(Story story, List<String> imagesUrl) {
        removeImages(story);
        List<Image> images = imageRepository.findAllByUrls(imagesUrl);
        if(images != null)  {
            images.stream().sorted(Comparator.comparing(Image::getOrderNum))
                    .forEach(image -> image.updateStory(story));
        }
    }

    @Transactional
    public void removeImages(Story story) {
        List<Image> oldImages = imageRepository.findAllByStory(story);

        Optional.ofNullable(oldImages).ifPresent(images -> images.forEach(img -> {
            s3ImageComponent.deleteImage(img.getUrl());
            story.removeImage(img);
        }));
    }

    @Override
    public Image loadEntity(UUID id) {
        return imageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
