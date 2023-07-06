package com.owori.domain.story.service;

import com.owori.domain.image.service.ImageService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAlbumStoryResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService implements EntityLoader<Story, Long> {

    private final StoryRepository storyRepository;
    private final StoryMapper storyMapper;
    private final ImageService imageService;
    private final MemberRepository memberRepository;

    @Transactional
    public IdResponse<Long> addStory(AddStoryRequest request) {
        Member member = memberRepository.findByNickname("지렁이");
        Story newStory = storyRepository.save(storyMapper.toEntity(request, member));
        imageService.updateStory(newStory, request.getImages());

        return new IdResponse<>(newStory.getId());
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

    @Override
    public Story loadEntity(Long id) {
        return storyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
