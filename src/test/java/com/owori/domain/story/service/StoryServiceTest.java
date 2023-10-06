package com.owori.domain.story.service;

import com.owori.domain.comment.entity.Comment;
import com.owori.domain.comment.repository.CommentRepository;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.heart.repository.HeartRepository;
import com.owori.domain.image.entity.Image;
import com.owori.domain.image.repository.ImageRepository;
import com.owori.domain.member.entity.Color;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.request.UpdateStoryRequest;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.dto.response.StoryIdResponse;
import com.owori.domain.story.dto.response.StoryPagingResponse;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Autowired private EntityManager em;


    @Test
    @DisplayName("이야기 생성이 수행되는가")
    void addStory() {
        //given
        String title = "기다리고 기다리던 하루";
        PostStoryRequest request = new PostStoryRequest(LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), title, "종강하면 동해바다로 가족 여행 가자고 한게 엊그제 같았는데...3박 4일 동해여행 너무 재밌었어!! 날씨도 너무 좋았고 특히 갈치조림이 대박 ㄹㅇ 맛집 인정... 2일차 점심 때 대림공원 안에서 피크닉한게 가장 기억에 남았던거 같아! 엄마가 만들어 준 샌드위치는 세상에서 젤 맛있어 이거 팔면 대박날듯 ㅋㅋㅋ ", null);

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
        StoryPagingResponse response = storyService.findAllStory(PageRequest.of(0, 4, Sort.by("created_at")));

        //then
        assertThat(response.getStories().get(0).getTitle()).isEqualTo(title);
        assertThat(response.getStories().get(0).getCommentCount()).isEqualTo(1);
        assertThat(response.getStories().get(0).getHeartCount()).isEqualTo(1);
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
        StoryPagingResponse response = storyService.findAllStory(PageRequest.of(0, 4, Sort.by("start_date")));

        //then
        assertThat(response.getStories().get(0).getTitle()).isEqualTo("기다리고 기다리던 하루");
        assertThat(response.getStories().get(0).getCommentCount()).isEqualTo(1);
        assertThat(response.getStories().get(0).getHeartCount()).isEqualTo(1);
        assertThat(response.getStories().get(1).getContent()).isEqualTo("내용2");
        assertThat(response.getStories().get(0).getThumbnail()).isEqualTo("a.png");
    }

    @Test
    @DisplayName("이야기 상세 조회가 제대로 수행되는가")
    void findStory() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        Story story = new Story("기다리고 기다리던 하루", "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);
        Image image = new Image("a.png", 1L);
        Image image2 = new Image("b.png", 2L);
        Comment comment = new Comment(member, story, null, "첫번째 댓글");

        familyRepository.save(family);
        storyRepository.save(story);
        heartRepository.save(new Heart(member, story));
        imageRepository.save(image);
        imageRepository.save(image2);
        commentRepository.save(comment);
        commentRepository.save(new Comment(member, story, comment, "두번째 댓글"));
        image.updateStory(story);
        image2.updateStory(story);

        //when
        FindStoryResponse response = storyService.findStory(story, null, true);

        //then
        assertThat(response.getIsLiked()).isEqualTo(true);

    }

    @Test
    @DisplayName("이야기 수정이 수행되는가")
    void updateStory() {
        //given
        Story story = new Story("기다리고 기다리던 하루", "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), authService.getLoginUser());
        Image image = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/Logo+(1).png", 1L);
        storyRepository.save(story);
        imageRepository.save(image);
        image.updateStory(story);


        Image image1 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/Logo+(2).png", 1L);
        Image image2 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/Logo+(3).png", 1L);
        imageRepository.save(image1);
        imageRepository.save(image2);

        UpdateStoryRequest request = new UpdateStoryRequest(story.getId(), LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), "제목", "내용", List.of("https://owori.s3.ap-northeast-2.amazonaws.com/story/Logo+(2).png","https://owori.s3.ap-northeast-2.amazonaws.com/story/Logo+(3).png"));

        //when
        StoryIdResponse response = storyService.updateStory(request);

        //then
        Story updateStory = storyRepository.findById(response.getStoryId()).get();

        assertThat(updateStory.getImages()).hasSize(2);
        assertThat(updateStory.getTitle()).isEqualTo("제목");
        assertThat(updateStory.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("이야기 삭제가 수행되는가")
    void removeStory() {
        //given
        Member member = authService.getLoginUser();
        Story story = new Story("기다리고 기다리던 하루", "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);
        storyRepository.save(story);

        //when
        storyService.removeStory(story);
        em.flush();
        em.clear();

        //then
        assertThrows(EntityNotFoundException.class, () -> storyService.loadEntity(story.getId()));

    }

    @Test
    @DisplayName("이야기 검색이 수행되는가")
    void findStoryBySearch() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        Story story = new Story("기다리고 기다리던 하루", "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);
        Story story2 = new Story("제목2", "내용", LocalDate.parse("2015-12-25"), LocalDate.parse("2015-12-30"), member);
        Story story3 = new Story("제목3", "내용 기다리고", LocalDate.parse("2015-12-25"), LocalDate.parse("2015-12-30"), member);

        familyRepository.save(family);
        member.updateProfile("파인애플",LocalDate.of(2000,04,22), Color.PINK);
        storyRepository.save(story);
        storyRepository.save(story2);
        storyRepository.save(story3);

        //when
        StoryPagingResponse response = storyService.findStoryBySearch("기다리", PageRequest.of(0, 4, Sort.by("created_at")));

        //then
        assertThat(response.getStories()).hasSize(2);
        assertThat(response.getStories().get(0).getContent()).isEqualTo("내용 기다리고");
        assertThat(response.getStories().get(0).getWriter()).isEqualTo("파인애플");
        assertThat(response.getStories().get(1).getTitle()).isEqualTo("기다리고 기다리던 하루");
    }

    @Test
    @DisplayName("유저가 작성한 이야기 조회가 수행되는가")
    void findStoryByWriter() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        Story story3 = new Story("제목3", "정답", LocalDate.parse("2015-12-25"), LocalDate.parse("2015-12-30"), member);

        familyRepository.save(family);
        member.updateProfile("파인애플",LocalDate.of(2000,04,22), Color.PINK);
        storyRepository.save(story3);

        //when
        StoryPagingResponse response = storyService.findStoryByWriter(PageRequest.of(0, 4, Sort.by("created_at")));

        //then
        assertThat(response.getStories()).hasSize(1);
        assertThat(response.getStories().get(0).getContent()).isEqualTo("정답");
        assertThat(response.getStories().get(0).getWriter()).isEqualTo("파인애플");
    }

    @Test
    @DisplayName("유저가 좋아한 이야기 조회가 수행되는가")
    void findStoryByHeart() {
        //given
        Member member = authService.getLoginUser();
        Story story1 = new Story("제목1", "이거 아님", LocalDate.parse("2015-12-25"), LocalDate.parse("2015-12-30"), member);
        Story story2 = new Story("좋아요", "정답", LocalDate.parse("2015-12-25"), LocalDate.parse("2015-12-30"), member);
        Story story3 = new Story("제목3", "이것도 아냐", LocalDate.parse("2015-12-25"), LocalDate.parse("2015-12-30"), member);

        storyRepository.save(story1);
        storyRepository.save(story2);
        storyRepository.save(story3);

        heartRepository.save(new Heart(member, story2));

        //when
        StoryPagingResponse response = storyService.findStoryByHeart(PageRequest.of(0, 4, Sort.by("start_date")));

        //then
        assertThat(response.getStories()).hasSize(1);
        assertThat(response.getStories().get(0).getContent()).isEqualTo("정답");
        assertThat(response.getStories().get(0).getTitle()).isEqualTo("좋아요");
    }
}
