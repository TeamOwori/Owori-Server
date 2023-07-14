package com.owori.domain.heart.service;

import com.owori.domain.heart.dto.HeartStatusResponse;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.heart.mapper.HeartMapper;
import com.owori.domain.heart.repository.HeartRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.service.StoryServiceCustom;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeartService implements EntityLoader<Heart, UUID> {
    private final HeartRepository heartRepository;
    private final HeartMapper heartMapper;
    private final StoryServiceCustom storyServiceCustom;
    private final AuthService authService;

    public HeartStatusResponse toggleHeart(UUID storyId){
        Member member = authService.getLoginUser();
        Story story = storyServiceCustom.loadEntity(storyId);

        if(hasHeart(member, story)){
            Heart heart = heartRepository.findByMemberAndStory(member, story).orElseThrow(EntityNotFoundException::new);
            story.removeHeart(heart);

            return new HeartStatusResponse(false);
        }

        Heart heart = heartMapper.toEntity(member, story);
        heartRepository.save(heart);

        return new HeartStatusResponse(true);
    }

    public boolean hasHeart(Member member, Story story){
        return heartRepository.existsByMemberAndStory(member, story);
    }

    @Override
    public Heart loadEntity(UUID id) {
        return heartRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
