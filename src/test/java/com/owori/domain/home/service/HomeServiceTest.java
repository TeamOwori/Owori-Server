package com.owori.domain.home.service;

import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.family.service.FamilyService;
import com.owori.domain.home.dto.response.FindHomeResponse;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
@DisplayName("Home 서비스의")
public class HomeServiceTest extends LoginTest {
    @Autowired private HomeService homeService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private FamilyService familyService;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private SayingRepository sayingRepository;
    @Autowired private AuthService authService;

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
        Saying saying3 = sayingRepository.save(new Saying("배고파", saveMember2, List.of()));

        // when
        FindHomeResponse responses = homeService.findHomeData();

        assertThat(familyName).isEqualTo(responses.getFamilyName());
    }
}
