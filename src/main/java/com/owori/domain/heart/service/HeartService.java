package com.owori.domain.heart.service;

import com.owori.domain.heart.dto.HeartStatusResponse;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.heart.mapper.HeartMapper;
import com.owori.domain.heart.repository.HeartRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final HeartMapper heartMapper;
    private final StoryService storyService;
    private final AuthService authService;

    public HeartStatusResponse toggleHeart(Long storyId){
        Member member = authService.getLoginUser();
        Story story = storyService.loadEntity(storyId);

        if(isAlreadyHeart(member, story)){
            Heart heart = heartRepository.findByMemberAndStory(member, story).orElseGet(null);
            story.removeHeart(heart);
            heartRepository.delete(heart);
            return new HeartStatusResponse(false);
        }

        Heart heart = heartMapper.toEntity(member, story);
        heartRepository.save(heart);
        story.addHeart(heart);

        return new HeartStatusResponse(true);
    }

    public boolean isAlreadyHeart(Member member, Story story){
        return heartRepository.findByMemberAndStory(member, story).isPresent();
    }

}
