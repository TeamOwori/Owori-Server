package com.owori.domain.story.service;

import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.AddStoryResponse;
import com.owori.domain.story.dto.response.findAlbumStoryResponse;
import com.owori.domain.story.dto.response.findListStoryResponse;
import com.owori.domain.story.dto.response.findStoryResponse;
import com.owori.domain.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;

    public AddStoryResponse addStory(AddStoryRequest request, List<MultipartFile> images) {
        // todo: 로직 작성
        return null;
    }

    public List<findAlbumStoryResponse> findAlbumStory(Pageable pageable, Long lastId) {
        // todo: 로직 작성
        return null;
    }

    public List<findListStoryResponse> findListStory(Pageable pageable, Long lastId) {
        // todo: 로직 작성
        return null;
    }

    public findStoryResponse findStory(Long storyId) {
        // todo: 로직 작성
        return null;
    }
}
