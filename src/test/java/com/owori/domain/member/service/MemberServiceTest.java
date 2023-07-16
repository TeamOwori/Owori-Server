package com.owori.domain.member.service;

import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.family.service.FamilyService;
import com.owori.domain.member.dto.request.EmotionalBadgeRequest;
import com.owori.domain.member.dto.request.MemberDetailsRequest;
import com.owori.domain.member.dto.request.MemberProfileRequest;
import com.owori.domain.member.dto.response.FindHomeResponse;
import com.owori.domain.member.dto.response.MemberProfileResponse;
import com.owori.domain.member.entity.*;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DatabaseTest
@DisplayName("Member 서비스의")
class MemberServiceTest extends LoginTest {
    @Autowired private MemberService memberService;
    @Autowired private FamilyService familyService;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private SayingRepository sayingRepository;
    @Autowired private EntityManager em;

    @Test
    @DisplayName("id로 조회가 수행되는가")
    void loadEntity() {
        //given

        //when
        Member member = memberService.loadEntity(loginUser.getId());

        //then
        assertThat(member).isEqualTo(loginUser);
    }

    @Test
    @DisplayName("멤버 조회 후 없을시 저장후 Jwt 토큰 생성 로직이 수행되는가")
    void saveIfNone() {
        //given

        //when
//        MemberJwtResponse response = memberService.saveIfNone(new MemberRequest("1233135135", AuthProvider.KAKAO));

        //then
//        JwtToken jwtToken = response.getJwtToken();
//        assertThat(jwtToken.getRefreshToken()).isNotBlank();
//        assertThat(jwtToken.getAccessToken()).isNotBlank();
//        assertThat(response.getMemberId().toString()).isNotBlank();
    }

    @Test
    @DisplayName("멤버 기본 정보 업데이트가 수행되는가")
    void updateMemberDetails() {
        //given
        LocalDate birthDay = authService.getLoginUser().getBirthDay();
        String nickname = authService.getLoginUser().getNickname();

        //when
        LocalDate now = LocalDate.now();
        String name = "123";
        memberService.updateMemberDetails(new MemberDetailsRequest(name, now));

        //then
        Member member = authService.getLoginUser();
        assertThat(birthDay).isNull();
        assertThat(nickname).isNull();
        assertThat(member.getBirthDay()).isEqualTo(now);
        assertThat(member.getNickname()).isEqualTo(name);
    }

    @Test
    @DisplayName("프로필 이미지 업데이트가 수행되는가")
    void updateMemberProfileImage() {
        //given

        //when

        //then

    }

    @Test
    @DisplayName("프로필 업데이트가 수행되는가")
    void updateProfile() {
        //given
        Family family = new Family("우리가족", loginUser, "1231231234");

        //when
        String nickname = "오월이";
        LocalDate birthday = LocalDate.now();
        Color color = Color.GREEN;
        memberService.updateMemberProfile(new MemberProfileRequest(nickname, birthday, color));

        //then
        Member member = memberService.loadEntity(loginUser.getId());
        assertThat(member.getBirthDay()).isEqualTo(birthday);
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("회원 탈퇴가 수행되는가")
    void deleteMember() {
        //given
        UUID id = loginUser.getId();

        //when
        memberService.deleteMember();
        em.flush();
        em.clear();

        //then
        assertThrows(EntityNotFoundException.class, () -> memberService.loadEntity(id));
    }

    @Test
    @DisplayName("감정 뱃지 업데이트가 수행되는가")
    void updateEmotionalBadge() {
        //given
        EmotionalBadge before = authService.getLoginUser().getEmotionalBadge();

        //when
        EmotionalBadge soHappy = EmotionalBadge.SO_HAPPY;
        memberService.updateEmotionalBadge(new EmotionalBadgeRequest(soHappy));
        em.flush();
        em.clear();

        //then
        assertThat(authService.getLoginUser().getEmotionalBadge()).isEqualTo(soHappy).isNotEqualTo(before);
    }

    @Test
    @DisplayName("홈 데이터 조회가 수행되는가")
    void findHomeData() {
        // given
        // 가족 생성
        String familyName = "오월이 가족";
        String code = familyService.saveFamily(new FamilyRequest(familyName)).getInviteCode();

        // 가족 구성원 생성
        Member member1 = Member.builder().oAuth2Info(new OAuth2Info("111111", AuthProvider.APPLE)).build();
        Member member2 = Member.builder().oAuth2Info(new OAuth2Info("222222", AuthProvider.APPLE)).build();
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
        FindHomeResponse responses = memberService.findHomeData();

        assertThat(familyName).isEqualTo(responses.getFamilyName());
        assertThat(responses.getMemberProfiles().stream().map(MemberProfileResponse::getId).toList()).isEqualTo(List.of(authService.getLoginUser().getId(), saveMember1.getId()));
        assertThat(responses.getSayings().stream().map(FindSayingByFamilyResponse::getId)).hasSameElementsAs(List.of(saying1.getId(), saying2.getId()));
    }
}
