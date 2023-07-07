package com.owori.domain.member.service;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.dto.request.MemberDetailsRequest;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Color;
import com.owori.domain.member.entity.Member;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@DatabaseTest
@DisplayName("Member 서비스의")
class MemberServiceTest extends LoginTest {
    @Autowired private MemberService memberService;

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
}
