package com.owori.domain.heart.service;

import com.owori.domain.heart.dto.HeartStatusResponse;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.heart.mapper.HeartMapper;
import com.owori.domain.heart.repository.HeartRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.service.StoryService;
import com.owori.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final HeartMapper heartMapper;
    private final StoryService storyService;
    private final AuthService authService;

    public HeartStatusResponse toggleHeart(UUID storyId){
        Member member = authService.getLoginUser();
        Story story = storyService.loadEntity(storyId);

        if(heartRepository.existsByMemberAndStory(member, story)){
            Heart heart = heartRepository.findByMemberAndStory(member, story).orElseThrow(EntityNotFoundException::new);
            story.removeHeart(heart);

            return new HeartStatusResponse(false);
        }

        Heart heart = heartMapper.toEntity(member, story);
        heartRepository.save(heart);

        return new HeartStatusResponse(true);
    }
}
