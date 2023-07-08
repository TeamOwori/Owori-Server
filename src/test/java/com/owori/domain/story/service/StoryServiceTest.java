package com.owori.domain.story.service;

import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAlbumStoryGroupResponse;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
@DisplayName("Story 서비스의")
public class StoryServiceTest extends LoginTest {

    @Autowired private StoryService storyService;
    @Autowired private StoryRepository storyRepository;

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
        Story story = storyRepository.findById(1L).orElseThrow();

        assertThat(story.getTitle()).isEqualTo(title);
        assertThat(story.getMember()).isEqualTo(authService.getLoginUser());
    }

    @Test
    @DisplayName("이야기 앨범형 최신순 조회가 수행되는가")
    void findAlbumStoryByCreateAt() {
        //given
        String title = "기다리고 기다리던 하루";
        storyRepository.save(new Story(title, "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), authService.getLoginUser()));

        //when
        List<FindAlbumStoryGroupResponse> findAlbumStoryGroupResponses = storyService.findAlbumStory(PageRequest.of(0, 4), "createAt", "7");

        //then
        assertThat(findAlbumStoryGroupResponses.get(0).getYearMonth()).isEqualTo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM")));
        assertThat(findAlbumStoryGroupResponses.get(0).getStories().get(0).getUrl()).isEqualTo(null);
    }

    @Test
    @DisplayName("이야기 앨범형 날짜순 조회가 수행되는가")
    void findAlbumStoryByEventAt() {
        //given
        String title = "기다리고 기다리던 하루";
        storyRepository.save(new Story(title, "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), authService.getLoginUser()));

        //when
        List<FindAlbumStoryGroupResponse> findAlbumStoryGroupResponses = storyService.findAlbumStory(PageRequest.of(0, 4), "eventAt", "2022.08");

        //then
        assertThat(findAlbumStoryGroupResponses.get(0).getYearMonth()).isEqualTo("2017.12");
        assertThat(findAlbumStoryGroupResponses.get(0).getStories().get(0).getUrl()).isEqualTo(null);
    }
}
