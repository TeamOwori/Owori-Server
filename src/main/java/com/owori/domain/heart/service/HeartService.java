package com.owori.domain.heart.service;

import com.owori.domain.heart.dto.response.HeartStatusResponse;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.heart.mapper.HeartMapper;
import com.owori.domain.heart.repository.HeartRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeartService implements EntityLoader<Heart, UUID> {
    private final HeartRepository heartRepository;
    private final HeartMapper heartMapper;
    private final AuthService authService;

    @Transactional
    public HeartStatusResponse toggleHeart(Story story){
        Member member = authService.getLoginUser();

        if(hasHeart(member, story)){
            Heart heart = heartRepository.findByMemberAndStory(member, story).orElseThrow(EntityNotFoundException::new);
            story.removeHeart(heart);

            return new HeartStatusResponse(Boolean.FALSE);
        }

        Heart heart = heartMapper.toEntity(member, story);
        heartRepository.save(heart);

        return new HeartStatusResponse(Boolean.FALSE);
    }

    public boolean hasHeart(Member member, Story story){
        return heartRepository.existsByMemberAndStory(member, story);
    }

    @Override
    public Heart loadEntity(UUID id) {
        return heartRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
