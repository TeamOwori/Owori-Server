package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class StoryIdResponse {
    private UUID storyId;
}
