package com.owori.domain.story.mapper;

import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryGroupResponse;
import com.owori.domain.story.dto.response.FindAllStoryResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Slice;
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
                .image(story.getMainImage())
                .heartCount(story.getHearts().size())
                .commentCount(story.getComments().size())
                .writer(story.getMember().getNickname())
                .startDate(story.getStartDate())
                .endDate(story.getEndDate())
                .build();
    }

    public FindStoryResponse toFindStoryResponse(Story story, boolean isLiked, List<CommentResponse> comments) {
        return FindStoryResponse.builder()
                .storyId(story.getId())
                .isLiked(isLiked)
                .images(story.getImageUrls())
                .title(story.getTitle())
                .writer(story.getMember().getNickname())
                .content(story.getContent())
                .heartCount(story.getHearts().size())
                .commentCount(story.getComments().size())
                .comments(comments)
                .build();
    }

    public FindAllStoryGroupResponse toFindAllStoryGroupResponse(Slice<Story> stories) {
        List<FindAllStoryResponse> responses = stories.getContent().stream()
                .map(this::toFindAllStoryResponse)
                .toList();

        return new FindAllStoryGroupResponse(responses, stories.hasNext());
    }
}