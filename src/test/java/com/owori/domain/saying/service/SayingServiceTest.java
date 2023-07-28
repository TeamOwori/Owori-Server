package com.owori.domain.saying.service;

import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.family.service.FamilyService;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.saying.dto.response.SayingIdResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DatabaseTest
@DisplayName("Saying 서비스의")
class SayingServiceTest extends LoginTest {
    @Autowired private SayingService sayingService;
    @Autowired private SayingRepository sayingRepository;
    @Autowired private FamilyService familyService;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private AuthService authService;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("서로에게 한마디 생성이 수행되는가")
    void addSaying() {
        // given
        String content = "오늘 집에 안 들어가요";
        AddSayingRequest request = new AddSayingRequest(content, List.of());

        // when
        SayingIdResponse response = sayingService.addSaying(request);

        // then
        Saying saying = sayingRepository.findById(response.getSayingId()).orElseThrow();

        assertThat(saying.getContent()).isEqualTo(content);
        assertThat(saying.getMember()).isEqualTo(authService.getLoginUser());
        assertThat(saying.getModifiable()).isTrue();
    }

    @Test
    @DisplayName("서로에게 한마디 수정이 수행되는가")
    void updateSaying() {
        // given
        String content = "오늘 집에 일찍 가요";
        Saying oldSaying = sayingRepository.save(new Saying("오늘 집에 안들어가요", authService.getLoginUser(), List.of()));
        UpdateSayingRequest request = new UpdateSayingRequest(oldSaying.getId(), content, List.of(UUID.randomUUID()));


        // when
        SayingIdResponse response = sayingService.updateSaying(request);
        Optional<Saying> newSaying = sayingRepository.findById(response.getSayingId());

        // then
        newSaying.ifPresent(saying -> assertThat(saying.getContent()).isEqualTo(content));
    }

    @Test
    @DisplayName("서로에게 한마디 삭제가 수행되는가")
    void deleteSaying() {
        // given
        Saying saying = sayingRepository.save(new Saying("오늘 집에 안들어감", authService.getLoginUser(), List.of()));

        // when
        sayingService.deleteSaying(saying.getId());
        Optional<Saying> deleteSaying = sayingRepository.findById(saying.getId());

        // then
        deleteSaying.ifPresent(value -> assertThat(value.getModifiable()).isFalse());
    }

    @Test
    @DisplayName("서로에게 한마디 가족별 조회가 수행되는가")
    void findSayingByFamily() {
        // given
        // 가족 생성
        String familyName = "오월이 가족";
        String code = familyService.saveFamily(new FamilyRequest(familyName)).getInviteCode();

        // 가족 구성원 생성
        Member member1 = Member.builder().oAuth2Info(new OAuth2Info("123123", AuthProvider.APPLE)).build();
        Member member2 = Member.builder().oAuth2Info(new OAuth2Info("123126", AuthProvider.APPLE)).build();
        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);

        // 가족에 멤버 추가
        Optional<Family> family = familyRepository.findByInviteCode(code);
        if(family.isPresent()) {
            family.get().addMember(saveMember1);
            family.get().addMember(authService.getLoginUser());
        }

        // 서로에게 한마디 생성
        Saying saying1 = sayingRepository.save(new Saying("오늘 집 안 감", authService.getLoginUser(), List.of()));
        Saying saying2 = sayingRepository.save(new Saying("오늘 집 감", saveMember1, List.of()));
        sayingRepository.save(new Saying("배고파", saveMember2, List.of()));

        // when
        entityManager.flush();
        entityManager.clear();
        List<SayingByFamilyResponse> responses = sayingService.findSayingByFamily();

        // then
        assertThat(responses.stream().map(SayingByFamilyResponse::getSayingId)).hasSameElementsAs(List.of(saying1.getId(), saying2.getId()));
    }

    @Test
    @DisplayName("id를 통한 조회가 수행되는가")
    void loadEntity() {
        // given
        Saying saying = sayingRepository.save(new Saying("오늘 집에 안들어갈래", authService.getLoginUser(), List.of()));

        // when
        Saying result = sayingService.loadEntity(saying.getId());

        // then
        assertThat(saying).isEqualTo(result);
    }
}
