package com.owori.domain.story.service;

import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.comment.service.CommentService;
import com.owori.domain.family.entity.Family;
import com.owori.domain.heart.service.HeartService;
import com.owori.domain.image.service.ImageService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.response.*;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.exception.InvalidUserException;
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
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class StoryService implements EntityLoader<Story, UUID> {

    private final StoryRepository storyRepository;
    private final StoryMapper storyMapper;
    private final ImageService imageService;
    private final CommentService commentService;
    private final HeartService heartService;
    private final AuthService authService;

    public IdResponse<UUID> addStory(PostStoryRequest request) {
        Member loginUser = authService.getLoginUser();
        Story newStory = storyRepository.save(storyMapper.toEntity(request, loginUser));
        List<UUID> imagesIds = request.getImagesId();
        if(imagesIds != null){
            imageService.updateStory(newStory, imagesIds);
        }
        return new IdResponse<>(newStory.getId());
    }

    public FindAllStoryGroupResponse findAllStory(Pageable pageable, LocalDate lastViewed) {
        Family family = authService.getLoginUser().getFamily();
        Slice<Story> storyBySlice = storyRepository.findAllStory(pageable, family, lastViewed);

        List<FindAllStoryResponse> stories = storyBySlice.getContent().stream()
                .map(story -> storyMapper.toFindAllStoryDto(story))
                .toList();

        return new FindAllStoryGroupResponse(stories, storyBySlice.hasNext());
    }

    public FindStoryResponse findStory(UUID storyId) {
        Member member = authService.getLoginUser();
        Story story = loadEntity(storyId);

        List<CommentResponse> comments = commentService.findComments(story, member);
        boolean isLiked = heartService.hasHeart(member, story);

        return storyMapper.toFindStoryDto(story, isLiked, comments);
    }

    public IdResponse<UUID> updateStory(UUID storyId, PostStoryRequest request) {
        Story story = loadEntity(storyId);
        if(story.getMember() != authService.getLoginUser()){
            throw new InvalidUserException();
        }
        updateStoryFields(story, request);
        return new IdResponse<>(story.getId());
    }

    public void updateStoryFields(Story story, PostStoryRequest request){
        ifPresentAndNonNull(request.getStartDate(), story::updateStartDate);
        ifPresentAndNonNull(request.getEndDate(), story::updateEndDate);
        ifPresentAndNonNull(request.getTitle(), story::updateTitle);
        ifPresentAndNonNull(request.getContents(), story::updateContents);
        ifPresentAndNonNull(request.getImagesId(), imagesId -> imageService.updateStory(story, imagesId));
    }

    public <T> void ifPresentAndNonNull(T value, Consumer<T> consumer) {
        Optional.ofNullable(value).ifPresent(consumer);
    }

    public void removeStory(UUID storyId) {
        Story story = loadEntity(storyId);
        if(story.getMember() != authService.getLoginUser()){
            throw new InvalidUserException();
        }

        // 이미지 삭제
        imageService.removeImages(story);

        // 댓글 삭제
        story.getComments().stream()
                .forEach(comment -> commentService.removeComment(comment.getId()));

        // 좋아요 삭제
        story.getHearts().stream().forEach(story::removeHeart);

        // 스토리 삭제
        story.delete();
    }


    @Override
    public Story loadEntity(UUID id) {
        return storyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
