package com.owori.domain.story.service;

import com.owori.domain.image.service.ImageService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.*;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.exception.StoryOrderException;
import com.owori.domain.story.mapper.StoryMapper;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StoryService implements EntityLoader<Story, Long> {

    private final StoryRepository storyRepository;
    private final StoryMapper storyMapper;
    private final ImageService imageService;
    private final AuthService authService;

    public IdResponse<Long> addStory(AddStoryRequest request) {
        Member loginUser = authService.getLoginUser();
        Story newStory = storyRepository.save(storyMapper.toEntity(request, loginUser));
        List<UUID> imagesIds = request.getImagesId();
        if(imagesIds != null){
            imageService.updateStory(newStory, imagesIds);
        }
        return new IdResponse<>(newStory.getId());
    }

    public FindAllStoryGroupResponse findAllStory(Pageable pageable, LocalDate lastViewed) {
        Member member = authService.getLoginUser();
        String sort = pageable.getSort().toList().get(0).getProperty();

        Slice<Story> storyBySlice;
        if (sort.equals("startDate")){
            storyBySlice = storyRepository.findAllStoryByEventAt(pageable, member, lastViewed);
        } else if (sort.equals("createdAt") || sort == null) {
            storyBySlice = storyRepository.findAllStoryByCreatedAt(pageable, member, lastViewed);
        } else {
            throw new StoryOrderException();
        }

        List<FindAllStoryResponse> stories = storyBySlice.getContent().stream()
                .map(story -> storyMapper.of(story))
                .toList();

        return new FindAllStoryGroupResponse(stories, storyBySlice.hasNext());
    }

    public FindStoryResponse findStory(Long storyId) {
        // todo: 로직 작성
        return null;
    }

    @Override
    public Story loadEntity(Long id) {
        return storyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
