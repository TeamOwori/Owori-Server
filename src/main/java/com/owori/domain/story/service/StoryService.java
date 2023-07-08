package com.owori.domain.story.service;

import com.owori.domain.image.service.ImageService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.collection.StoryAlbumGroup;
import com.owori.domain.story.dto.collection.StoryGroupByYearMonth;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAlbumStoryGroupResponse;
import com.owori.domain.story.dto.response.FindListStoryResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        List<UUID> imageIds = request.getImageId();
        if(imageIds != null){
            imageService.updateStory(newStory, imageIds);
        }
        return new IdResponse<>(newStory.getId());
    }

    public List<FindAlbumStoryGroupResponse> findAlbumStory(Pageable pageable, LocalDate lastViewed) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM");
        String sort = pageable.getSort().toList().get(0).getProperty();
        Member loginUser = authService.getLoginUser();

        Slice<Story> storyBySlice;
        Map<String, List<Story>> groupedStories;
        if (sort.equals("startDate")){
            storyBySlice = storyRepository.findAllStoryByEventAt(pageable, lastViewed, loginUser);
            groupedStories = storyBySlice.getContent().stream().collect(Collectors.groupingBy(s -> s.getStartDate().format(formatter)));
        } else if (sort.equals("createAt") || sort == null) {
            storyBySlice = storyRepository.findAllStoryByCreateAt(pageable, lastViewed, loginUser);
            groupedStories = storyBySlice.getContent().stream().collect(Collectors.groupingBy(s -> s.getBaseTime().getCreatedAt().format(formatter)));
        } else {
            throw new StoryOrderException();
        }

        // yyyy.MM로 grouping 후 내림차순 정렬
        StoryGroupByYearMonth storyGroupByYearMonth = new StoryGroupByYearMonth();
        storyGroupByYearMonth.addStories(groupedStories);
        Map<String, List<Story>> storyByYearMonth = storyGroupByYearMonth.getStoryByYearMonth();

        // dto로 변환
        StoryAlbumGroup storyAlbumGroup = new StoryAlbumGroup(storyByYearMonth);
        return storyAlbumGroup.getStoryGroupResponses(storyBySlice);
    }

    public List<FindListStoryResponse> findListStory(Pageable pageable, Long lastId) {
        // todo: 로직 작성
        return null;
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
