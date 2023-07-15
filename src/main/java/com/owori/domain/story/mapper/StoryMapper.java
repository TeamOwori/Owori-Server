package com.owori.domain.story.mapper;

import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.entity.Story;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class StoryMapper {
    public Story toEntity(PostStoryRequest request, Member member){
        return Story.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .member(member)
                .build();
    }

    public FindAllStoryResponse toFindAllStoryDto(Story story){
        return FindAllStoryResponse.builder()
                .id(story.getId())
                .title(story.getTitle())
                .contents(story.getContents())
                .image(story.getMainImage())
                .heartCnt(story.getHearts().size())
                .commentCnt(story.getComments().size())
                .writer(story.getMember().getNickname())
                .startDate(story.getStartDate())
                .endDate(story.getEndDate())
                .build();
    }

    public FindStoryResponse toFindStoryDto(Story story, boolean isLiked, List<CommentResponse> comments){
        return FindStoryResponse.builder()
                .id(story.getId())
                .isLiked(isLiked)
                .images(story.getImageUrls())
                .title(story.getTitle())
                .writer(story.getMember().getNickname())
                .contents(story.getContents())
                .HeartCnt(story.getHearts().size())
                .CommentCnt(story.getComments().size())
                .comments(comments)
                .build();
    }
}