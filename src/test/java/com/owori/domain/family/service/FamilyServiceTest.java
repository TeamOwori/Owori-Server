package com.owori.domain.family.service;

import com.owori.domain.family.dto.request.AddMemberRequest;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DatabaseTest
@DisplayName("Family 서비스의")
class FamilyServiceTest extends LoginTest {
    @Autowired private FamilyService familyService;
    @Autowired private FamilyRepository familyRepository;

    @Test
    @DisplayName("가족 생성이 수행되는가")
    void saveFamily() {
        //given
        String familyName = "오월이 가족";

        //when
        InviteCodeResponse result = familyService.saveFamily(new FamilyRequest(familyName));

        //then
        Family family = familyRepository.findByInviteCode(result.getInviteCode()).orElseThrow();

        assertThat(family.getFamilyGroupName()).isEqualTo(familyName);
        assertThat(family.getMembers()).hasSize(1).hasSameElementsAs(Set.of(loginUser));
    }

    @Test
    @DisplayName("인증을 통한 멤버 추가가 수행되는가")
    void addMember() {
        //given
        String familyName = "오월이 가족";
        String code = familyService.saveFamily(new FamilyRequest(familyName)).getInviteCode();

        Member member = Member.builder().oAuth2Info(new OAuth2Info("123123", AuthProvider.APPLE)).build();
        when(authService.getLoginUser()).thenReturn(member);

        //when
        familyService.addMember(new AddMemberRequest(code));

        //then
        Family family = familyRepository.findByInviteCode(code).orElseThrow();
        assertThat(family.getMembers()).hasSize(2).hasSameElementsAs(Set.of(member, loginUser));
    }

    @Test
    @DisplayName("id를 통한 조회가 수행되는가")
    void loadEntity() {
        //given
        Family family = familyRepository.save(Family.builder().member(loginUser).build());

        //when
        Family result = familyService.loadEntity(family.getId());

        //then
        assertThat(family).isEqualTo(result);
    }
}
