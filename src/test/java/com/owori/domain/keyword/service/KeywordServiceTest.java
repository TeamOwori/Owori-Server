package com.owori.domain.keyword.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.keyword.entity.Keyword;
import com.owori.domain.keyword.dto.response.FindKeywordsResponse;
import com.owori.domain.keyword.repository.KeywordRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.domain.story.service.StoryService;
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
@DisplayName("Keyword 서비스의")
public class KeywordServiceTest extends LoginTest {

    @Autowired private KeywordService keywordService;
    @Autowired private KeywordRepository keywordRepository;
    @Autowired private AuthService authService;
    @Autowired private StoryRepository storyRepository;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private StoryService storyService;
    @Autowired private EntityManager em;



    @Test
    @DisplayName("검색어가 저장되는가")
    void addStorySearchWord() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        Story story = new Story("기다리고 기다리던 하루", "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);

        familyRepository.save(family);
        storyRepository.save(story);

        //when
        storyService.findStoryBySearch("기다리", PageRequest.of(0, 4, Sort.by("createdAt")), null);
        storyService.findStoryBySearch("우리집", PageRequest.of(0, 4, Sort.by("createdAt")), null);
        storyService.findStoryBySearch("기다리", PageRequest.of(0, 4, Sort.by("createdAt")), null);

        List<Keyword> keywords = keywordRepository.findByMember(member);

        //then
        assertThat(keywords.get(0).getMember()).isEqualTo(member);
        assertThat(keywords.size()).isEqualTo(2);
        assertThat(keywords.get(0).getContents()).isEqualTo("기다리");
        assertThat(keywords.get(1).getContents()).isEqualTo("우리집");
    }

    @Test
    @DisplayName("검색어 조회가 되는가")
    void findAllStorySearchWord() {
        //given
        Member member = authService.getLoginUser();
        Family family = new Family("우리집", member, "code");
        Story story = new Story("기다리고 기다리던 하루", "내용", LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), member);

        familyRepository.save(family);
        storyRepository.save(story);

        //when
        storyService.findStoryBySearch("야호", PageRequest.of(0, 4, Sort.by("createdAt")), null);
        storyService.findStoryBySearch("우리집", PageRequest.of(0, 4, Sort.by("createdAt")), null);
        storyService.findStoryBySearch("야호", PageRequest.of(0, 4, Sort.by("createdAt")), null);

        List<FindKeywordsResponse> searchWords = keywordService.findSearchWords();

        //then
        assertThat(searchWords.size()).isEqualTo(2);
        assertThat(searchWords.get(0).getContents()).isEqualTo("야호");
        assertThat(searchWords.get(1).getContents()).isEqualTo("우리집");
    }

    @Test
    @DisplayName("검색어 단일 삭제가 실행되는가")
    void removeStorySearchWord() {
        //given
        Member member = authService.getLoginUser();
        Keyword keyword = new Keyword("검색어 삭제 테스트", member);
        keywordRepository.save(keyword);

        //when
        keywordService.deleteSearchWord(keyword.getId());
        em.flush();
        em.clear();

        //then
        assertThrows(EntityNotFoundException.class, () -> keywordService.loadEntity(keyword.getId()));

    }

    @Test
    @DisplayName("검색어 전체 삭제가 실행되는가")
    void removeAllStorySearchWord() {
        //given
        Member member = authService.getLoginUser();
        Keyword keyword = new Keyword("검색어", member);
        Keyword keyword1 = new Keyword("전체", member);
        Keyword keyword2 = new Keyword("삭제 테스트", member);
        keywordRepository.save(keyword);
        keywordRepository.save(keyword1);
        keywordRepository.save(keyword2);

        //when
        keywordService.deleteSearchWords();
        em.flush();
        em.clear();

        //then
        assertThrows(EntityNotFoundException.class, () -> keywordService.loadEntity(keyword.getId()));
        assertThrows(EntityNotFoundException.class, () -> keywordService.loadEntity(keyword1.getId()));
        assertThrows(EntityNotFoundException.class, () -> keywordService.loadEntity(keyword2.getId()));

    }
}
