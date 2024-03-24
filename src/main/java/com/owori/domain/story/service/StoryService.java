package com.owori.domain.story.service;

import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.family.entity.Family;
import com.owori.domain.heart.service.HeartService;
import com.owori.domain.image.service.ImageService;
import com.owori.domain.keyword.service.KeywordService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.request.UpdateStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryGroupResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.dto.response.StoryIdResponse;
import com.owori.domain.story.dto.response.StoryPagingResponse;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.mapper.StoryMapper;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.exception.InvalidDateException;
import com.owori.global.exception.NoAuthorityException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoryService implements EntityLoader<Story, UUID> {

    private final StoryRepository storyRepository;
    private final StoryMapper storyMapper;
    private final ImageService imageService;
    private final HeartService heartService;
    private final AuthService authService;
    private final KeywordService keywordService;

    public StoryIdResponse addStory(PostStoryRequest request) {
        validateDate(request.getStartDate(), request.getEndDate());
        Member loginUser = authService.getLoginUser();
        Story newStory = storyRepository.save(storyMapper.toEntity(request, loginUser));
        List<String> imagesUrl = request.getStoryImages();
        if (imagesUrl != null) {
            imageService.updateStory(newStory, imagesUrl);
        }
        return new StoryIdResponse(newStory.getId());
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)) throw new InvalidDateException();
    }

    @Transactional(readOnly = true)
    public StoryPagingResponse findAllStory(Pageable pageable) {
        Family family = authService.getLoginUser().getFamily();
        return storyMapper.toStoryPagingResponse(storyRepository.findAllStory(pageable, family));
    }

    public FindAllStoryGroupResponse findAllStory2(String sort) {
        Family family = authService.getLoginUser().getFamily();
        List<Story> familyStories = findFamilyStories(sort, family);
        return storyMapper.toFindAllStoryGroupResponse2(familyStories);
    }

    private List<Story> findFamilyStories(String sort, Family family) {
        if(sort.equals("created_at")) return storyRepository.findAllByFamilyOrderByCreatedAt(family);
        return storyRepository.findAllByFamilyOrderByStartDate(family);
    }

    public FindStoryResponse findStory(Story story, List<CommentResponse> comments, boolean isLiked) {
        return storyMapper.toFindStoryResponse(story, isLiked, comments);
    }

    @Transactional
    public StoryIdResponse updateStory(UpdateStoryRequest request) {
        validateDate(request.getStartDate(), request.getEndDate());
        Story story = loadEntity(request.getStoryId());
        if (!story.getMember().getId().equals(authService.getLoginUser().getId())) {
            throw new NoAuthorityException();
        }
        story.update(request.getContent(), request.getTitle(), request.getStartDate(), request.getEndDate());
        imageService.updateStory(story, request.getStoryImages());

        return new StoryIdResponse(story.getId());
    }

    @Transactional
    public void removeStory(Story story) {
        if (!story.getMember().getId().equals(authService.getLoginUser().getId())) {
            throw new NoAuthorityException();
        }

        imageService.removeImages(story); // 이미지 삭제
        story.getHearts().forEach(story::removeHeart); // 좋아요 삭제
        story.delete(); // 스토리 삭제
    }

    public StoryPagingResponse findStoryBySearch(String keyword, Pageable pageable) {
        Member loginUser = authService.getLoginUser();
        keywordService.addKeyword(keyword, loginUser);

        return storyMapper.toStoryPagingResponse(storyRepository.findStoryBySearch(pageable, keyword, loginUser.getFamily()));
    }

    public FindAllStoryGroupResponse findStoryBySearch2(String keyword, String sort) {
        Member loginUser = authService.getLoginUser();
        List<Story> storyBySearch = storyRepository.findStoryBySearch2(keyword, loginUser.getFamily());
        keywordService.addKeyword(keyword, loginUser);
        return storyMapper.toFindAllStoryGroupResponse2(storyBySearch);
    }

    @Transactional(readOnly = true)
    public StoryPagingResponse findStoryByWriter(Pageable pageable) {
        Member member = authService.getLoginUser();
        return storyMapper.toStoryPagingResponse(storyRepository.findStoryByWriter(pageable, member));
    }

    public FindAllStoryGroupResponse findStoryByWriter2() {
        Member member = authService.getLoginUser();
        List<Story> storyByWriter = storyRepository.findAllByMember(member);

        return storyMapper.toFindAllStoryGroupResponse2(storyByWriter);
    }

    @Transactional(readOnly = true)
    public StoryPagingResponse findStoryByHeart(Pageable pageable) {
        Member member = authService.getLoginUser();
        return storyMapper.toStoryPagingResponse(storyRepository.findStoryByHeart(pageable, member));
    }

    public FindAllStoryGroupResponse findStoryByHeart2() {
        Member member = authService.getLoginUser();
        List<Story> storyByHeart = heartService.findStoryByMember(member);

        return storyMapper.toFindAllStoryGroupResponse2(storyByHeart);
    }

    @Override
    public Story loadEntity(UUID id) {
        return storyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Long findStoryNumByMember(Member member) {
        return storyRepository.countByMember(member);
    }

    public List<Story> findStoriesByWriter(Member writer) {
        return storyRepository.findAllByMember(writer);
    }
}
