package com.owori.domain.story.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
@DisplayName("Facade 서비스의")
public class FacadeServiceTest extends LoginTest {

    @Autowired
    private FacadeService facadeService;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private StoryRepository storyRepository;


    @Test
    @DisplayName("id로 Story 조회가 잘 이루어지는가")
    void findStory() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        String title = "기다리고 기다리던 하루";
        Story story = new Story(title, "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);

        familyRepository.save(family);
        storyRepository.save(story);

        //when
        FindStoryResponse findStory = facadeService.findStory(story.getId());

        //then
        assertThat(findStory.getStoryId()).isEqualTo(story.getId());
        assertThat(findStory.getTitle()).isEqualTo(title);

    }

    @Test
    @DisplayName("id로 Story Entity 조회가 잘 이루어지는가")
    void loadStoryEntity() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        String title = "기다리고 기다리던 하루";
        Story story = new Story(title, "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);

        familyRepository.save(family);
        storyRepository.save(story);

        //when
        Story findStory = facadeService.loadStoryEntity(story.getId());

        //then
        assertThat(findStory.getId()).isEqualTo(story.getId());
        assertThat(findStory.getTitle()).isEqualTo(title);

    }
}