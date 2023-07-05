package com.owori.domain.story.service;

import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAlbumStoryResponse;
import com.owori.domain.story.dto.response.FindListStoryResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;

    public IdResponse<Long> addStory(AddStoryRequest request, List<MultipartFile> images) {
        // todo: 로직 작성
        return null;
    }

    public List<FindAlbumStoryResponse> findAlbumStory(Pageable pageable, Long lastId) {
        // todo: 로직 작성
        return null;
    }

    public List<FindListStoryResponse> findListStory(Pageable pageable, Long lastId) {
        // todo: 로직 작성
        return null;
    }

    public FindStoryResponse findStory(Long storyId) {
        // todo: 로직 작성
        return null;
    }
}
