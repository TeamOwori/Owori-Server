package com.owori.domain.heart.service;

import com.owori.domain.heart.dto.HeartStatusResponse;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.heart.repository.HeartRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
@DisplayName("Heart 서비스의")
public class HeartServiceTest extends LoginTest {
    @Autowired HeartService heartService;

    @Autowired HeartRepository heartRepository;
    @Autowired StoryRepository storyRepository;
    @Autowired AuthService authService;


    @Test
    @DisplayName("좋아요가 생성이 이루어지는가")
    void addHeart() {
        //given
        Member member = authService.getLoginUser();
        Story story = new Story("좋아요 생성이 제대로", "될까요 ?", LocalDate.of(2000, 4, 22), LocalDate.of(2022, 8, 22), member);
        storyRepository.save(story);

        //when
        HeartStatusResponse response = heartService.toggleHeart(story.getId());

        //then
        Set<Heart> hearts = storyRepository.findById(story.getId()).get().getHearts();
        Heart heart = heartRepository.findByMemberAndStory(member, story).get();

        assertThat(response.getIsLiked()).isEqualTo(true);
        assertThat(heart.getStory()).isEqualTo(story);
        assertThat(heart.getMember()).isEqualTo(member);
        assertThat(hearts.iterator().next()).isEqualTo(heart);
    }

    @Test
    @DisplayName("좋아요가 눌려있을 때, 다시 누르면 좋아요가 사라지는가")
    void addHeartIsAlready() {
        //given
        Member member = authService.getLoginUser();
        Story story = new Story("좋아요가 제대로", "없어졌을까요 ?", LocalDate.of(2000, 4, 22), LocalDate.of(2022, 8, 22), member);
        storyRepository.save(story);
        heartRepository.save(new Heart(member, story));

        //when
        HeartStatusResponse response = heartService.toggleHeart(story.getId());

        //then
        Set<Heart> hearts = storyRepository.findById(story.getId()).get().getHearts();
        Heart heart = heartRepository.findByMemberAndStory(member, story).orElse(null);

        assertThat(response.getIsLiked()).isEqualTo(false);
        assertThat(heart).isEqualTo(null);
        assertThat(hearts.isEmpty()).isEqualTo(true);
    }
}
