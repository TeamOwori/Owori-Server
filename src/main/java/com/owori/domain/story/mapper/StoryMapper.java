package com.owori.domain.story.mapper;

import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryGroupResponse;
import com.owori.domain.story.dto.response.FindAllStoryResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.dto.response.StoryPagingResponse;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class StoryMapper {
    public Story toEntity(PostStoryRequest request, Member member) {
        return Story.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .member(member)
                .build();
    }

    public FindAllStoryResponse toFindAllStoryResponse(Story story) {
        return FindAllStoryResponse.builder()
                .storyId(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .thumbnail(story.getMainImage())
                .isMultipleImages(story.isMultipleImages())
                .heartCount(story.getHearts().size())
                .commentCount(story.getComments().size())
                .writer(checkIsNull(story))
                .startDate(story.getStartDate())
                .endDate(story.getEndDate())
                .build();
    }

    public FindStoryResponse toFindStoryResponse(Story story, boolean isLiked, List<CommentResponse> comments) {
        return FindStoryResponse.builder()
                .storyId(story.getId())
                .isLiked(isLiked)
                .storyImages(story.getImageUrls())
                .title(story.getTitle())
                .writer(checkIsNull(story))
                .content(story.getContent())
                .heartCount(story.getHearts().size())
                .commentCount(story.getComments().size())
                .comments(comments)
                .startDate(story.getStartDate())
                .endDate(story.getEndDate())
                .thumbnail(story.getMainImage())
                .build();
    }

    public StoryPagingResponse toStoryPagingResponse(Page<Story> pageStory) {
        List<FindAllStoryResponse> stories = pageStory.getContent().stream().map(story -> toFindAllStoryResponse(story)).toList();
        return StoryPagingResponse.builder()
                .stories(stories)
                .hasNext(pageStory.hasNext())
                .build();
    }

    public FindAllStoryGroupResponse toFindAllStoryGroupResponse2(List<Story> stories) {
        List<FindAllStoryResponse> responses = stories.stream()
                .map(this::toFindAllStoryResponse)
                .toList();
        return new FindAllStoryGroupResponse(responses, Boolean.FALSE);
    }

    private String checkIsNull(Story story) {
        if(story.getMember() == null) return "탈퇴한 사용자";
        return story.getMember().getNickname();
    }
}