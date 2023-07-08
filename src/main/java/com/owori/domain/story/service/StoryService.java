package com.owori.domain.story.service;

import com.owori.domain.image.entity.Image;
import com.owori.domain.image.service.ImageService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAlbumStoryResponse;
import com.owori.domain.story.dto.response.FindAlbumStoryGroupResponse;
import com.owori.domain.story.dto.response.FindListStoryResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.entity.Story;
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

    public Map<String, List<Story>> groupStoryByYearMonth (List<Story> storyList, String orderBy){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM");

        if(orderBy.equals("eventAt")) {
            return storyList.stream()
                    .collect(Collectors.groupingBy(story -> story.getStartDate().format(formatter)));
        }
        else {
            return storyList.stream()
                    .collect(Collectors.groupingBy(story -> story.getBaseTime().getCreatedAt().format(formatter)));
        }
    }

    public List<FindAlbumStoryGroupResponse> findAlbumStory(Pageable pageable, String orderBy, LocalDate lastViewed) {
        Member loginUser = authService.getLoginUser();

        Slice<Story> storyBySlice;
        if (orderBy.equals("eventAt")){
            storyBySlice = storyRepository.findAllStoryByEventAt(pageable, lastViewed, loginUser);
        }
        else {
            storyBySlice = storyRepository.findAllStoryByCreateAt(pageable, lastViewed, loginUser);
        }

        // yyyy.MM로 grouping 후 내림차순 정렬
        Map<String, List<Story>> groupedStories =  groupStoryByYearMonth(storyBySlice.getContent(), orderBy);
        Map<String, List<Story>> StoryByYearMonth  = new TreeMap<>(Collections.reverseOrder());
        StoryByYearMonth.putAll(groupedStories);


        return StoryByYearMonth.entrySet().stream()
                .map(entry -> {
                    FindAlbumStoryGroupResponse storyGroupResponse = new FindAlbumStoryGroupResponse(entry.getKey());
                    List<FindAlbumStoryResponse> storyResponseList = entry.getValue().stream()
                            .map(story -> {
                                String imageUrl = story.getImages().stream()
                                        .findFirst().map(Image::getUrl).orElse(null);
                                return new FindAlbumStoryResponse(story.getId(), imageUrl);
                            }).toList();
                    return storyGroupResponse.updateStories(storyResponseList, storyBySlice.hasNext());
                }).toList();

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
