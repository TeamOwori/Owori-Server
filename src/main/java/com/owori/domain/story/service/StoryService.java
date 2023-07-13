package com.owori.domain.story.service;

import com.owori.domain.image.service.ImageService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.collection.StoryAlbumGroup;
import com.owori.domain.story.dto.collection.StoryGroupByYearMonth;
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
        List<UUID> imagesIds = request.getImagesId();
        if(imagesIds != null){
            imageService.updateStory(newStory, imagesIds);
        }
        return new IdResponse<>(newStory.getId());
    }

    public FindAlbumStoryGroupResponse findAlbumStory(Pageable pageable, LocalDate lastViewed) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM");
        String sort = pageable.getSort().toList().get(0).getProperty();
        Member loginUser = authService.getLoginUser();

        Slice<Story> storyBySlice;
        Map<String, List<Story>> groupedStories;
        if (sort.equals("startDate")){
            storyBySlice = storyRepository.findAllAlbumStoryByEventAt(pageable, lastViewed, loginUser);
            groupedStories = storyBySlice.getContent().stream().collect(Collectors.groupingBy(s -> s.getStartDate().format(formatter)));
        } else if (sort.equals("createdAt") || sort == null) {
            storyBySlice = storyRepository.findAllAlbumStoryByCreatedAt(pageable, lastViewed, loginUser);
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
        return new FindAlbumStoryGroupResponse(storyAlbumGroup.getStoryGroupResponses(), storyBySlice.hasNext());
    }

    public FindListStoryGroupResponse findListStory(Pageable pageable, LocalDate lastViewed) {
        Member member = authService.getLoginUser();
        String sort = pageable.getSort().toList().get(0).getProperty();

        Slice<Story> storyBySlice;
        if (sort.equals("startDate")){
            storyBySlice = storyRepository.findAllListStoryByEventAt(pageable, member, lastViewed);
        } else if (sort.equals("createdAt") || sort == null) {
            storyBySlice = storyRepository.findAllListStoryByCreatedAt(pageable, member, lastViewed);
        } else {
            throw new StoryOrderException();
        }

        List<FindListStoryResponse> stories = storyBySlice.getContent().stream()
                .map(s -> new FindListStoryResponse(
                        s.getId(), s.getTitle(), s.getContents(),
                        s.getImages() == null || s.getImages().isEmpty() ? null : s.getImages().get(0).getUrl(),
                        s.getHearts().size(), s.getComments().size(), s.getMember().getNickname(), s.getStartDate(), s.getEndDate()))
                .toList();

        return new FindListStoryGroupResponse(stories, storyBySlice.hasNext());
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
