package com.owori.domain.story.service;

import com.owori.domain.comment.entity.Comment;
import com.owori.domain.comment.repository.CommentRepository;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.heart.repository.HeartRepository;
import com.owori.domain.image.entity.Image;
import com.owori.domain.image.repository.ImageRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryGroupResponse;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
@DisplayName("Story 서비스의")
public class StoryServiceTest extends LoginTest {

    @Autowired private StoryService storyService;
    @Autowired private StoryRepository storyRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private HeartRepository heartRepository;
    @Autowired private ImageRepository imageRepository;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private AuthService authService;


    @Test
    @DisplayName("이야기 생성이 수행되는가")
    void addStory() {
        //given
        String title = "기다리고 기다리던 하루";
        AddStoryRequest request = new AddStoryRequest(LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), title, "종강하면 동해바다로 가족 여행 가자고 한게 엊그제 같았는데...3박 4일 동해여행 너무 재밌었어!! 날씨도 너무 좋았고 특히 갈치조림이 대박 ㄹㅇ 맛집 인정... 2일차 점심 때 대림공원 안에서 피크닉한게 가장 기억에 남았던거 같아! 엄마가 만들어 준 샌드위치는 세상에서 젤 맛있어 이거 팔면 대박날듯 ㅋㅋㅋ ", null);

        //when
        storyService.addStory(request);

        //then
        Story story = storyRepository.findAll().get(0);

        assertThat(story.getTitle()).isEqualTo(title);
        assertThat(story.getMember()).isEqualTo(authService.getLoginUser());
    }

    @Test
    @DisplayName("최신순 이야기 전체 조회가 수행되는가")
    void findAllStoryByCreatedAt() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        String title = "기다리고 기다리던 하루";
        Story story = new Story(title, "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);

        familyRepository.save(family);
        storyRepository.save(story);
        commentRepository.save(new Comment(member, story,null, "댓글"));
        heartRepository.save(new Heart(member, story));

        //when
        FindAllStoryGroupResponse response = storyService.findAllStory(PageRequest.of(0, 4, Sort.by("createdAt")), null);

        //then
        assertThat(response.getStories().get(0).getTitle()).isEqualTo(title);
        assertThat(response.getStories().get(0).getCommentCnt()).isEqualTo(1);
        assertThat(response.getStories().get(0).getHeartCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("날짜순 이야기 전체 조회가 수행되는가")
    void findAllStoryByEventAt() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        Story story = new Story("기다리고 기다리던 하루", "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);
        Story story2 = new Story("제목2", "내용2", LocalDate.parse("2015-12-25"), LocalDate.parse("2015-12-30"), member);
        Image image = new Image("a.png", 1L);

        familyRepository.save(family);
        storyRepository.save(story);
        storyRepository.save(story2);
        commentRepository.save(new Comment(member, story,null, "댓글"));
        heartRepository.save(new Heart(member, story));
        imageRepository.save(image);
        image.updateStory(story);

        //when
        FindAllStoryGroupResponse response = storyService.findAllStory(PageRequest.of(0, 4, Sort.by("startDate")), null);

        //then
        assertThat(response.getStories().get(0).getTitle()).isEqualTo("기다리고 기다리던 하루");
        assertThat(response.getStories().get(0).getCommentCnt()).isEqualTo(1);
        assertThat(response.getStories().get(0).getHeartCnt()).isEqualTo(1);
        assertThat(response.getStories().get(1).getContents()).isEqualTo("내용2");
        assertThat(response.getStories().get(0).getImage()).isEqualTo("a.png");
    }
}
