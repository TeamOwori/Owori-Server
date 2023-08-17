package com.owori.domain.member.controller;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.dto.request.*;
import com.owori.domain.member.dto.response.*;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Color;
import com.owori.domain.member.entity.EmotionalBadge;
import com.owori.domain.member.service.MemberService;
import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import com.owori.domain.schedule.entity.ScheduleType;
import com.owori.support.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member 컨트롤러의")
@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocsTest {
    @MockBean private MemberService memberService;

    @Test
    @DisplayName("멤버 생성 후 카카오 JwtToken 생성이 수행되는가")
    void saveMemberWithKakao() throws Exception {
        //given
        JwtToken jwt = new JwtToken("accesasdfagfwaerg.tokenasfd13sad.isthisahtfgwiueoh", "refreshriuqwhfoieu.tokenqiweurhu.isthiswheoituhw");
        MemberJwtResponse expected = new MemberJwtResponse(UUID.randomUUID(), Boolean.TRUE, jwt);
        when(memberService.saveWithKakaoIfNone(any())).thenReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/kakao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new MemberKakaoRequest("382y5e3a5", AuthProvider.KAKAO))));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt_token.access_token").exists())
                .andExpect(jsonPath("$.jwt_token.refresh_token").exists())
                .andExpect(jsonPath("$.member_id").exists());

        //docs
        perform.andDo(print())
                .andDo(document("save member by kakao and get jwt", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 생성 후 애플 JwtToken 생성이 수행되는가")
    void saveMemberWithApple() throws Exception {
        //given
        JwtToken jwt = new JwtToken("accesasdfagfwaerg.tokenasfd13sad.isthisahtfgwiueoh", "refreshriuqwhfoieu.tokenqiweurhu.isthiswheoituhw");
        MemberJwtResponse expected = new MemberJwtResponse(UUID.randomUUID(), Boolean.TRUE, jwt);
        when(memberService.saveWithAppleIfNone(any())).thenReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/apple")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new MemberAppleRequest("382y5e3a5","code", AuthProvider.APPLE))));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt_token.access_token").exists())
                .andExpect(jsonPath("$.jwt_token.refresh_token").exists())
                .andExpect(jsonPath("$.member_id").exists());

        //docs
        perform.andDo(print())
                .andDo(document("save member by apple and get jwt", getDocumentRequest(), getDocumentResponse()));
    }
    @Test
    @DisplayName("멤버 기본 정보 업데이트가 수행되는가")
    void updateMemberDetails() throws Exception {
        //given

        when(memberService.updateMemberDetails(any())).thenReturn(new MemberValidateResponse(true));

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new MemberDetailsRequest("owori", LocalDate.now())))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString()));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("update member details", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 프로필 이미지 저장이 수행되는가")
    void updateMemberProfileImage() throws Exception {
        //given
        MemberImageResponse expected = new MemberImageResponse("http://someImageToUrl");
        given(memberService.updateMemberProfileImage(any())).willReturn(expected);
        MockMultipartFile image1 = new MockMultipartFile("profile_image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "Image".getBytes(StandardCharsets.UTF_8));

        //when
        ResultActions perform =
                mockMvc.perform(
                        multipart("/members/profile-image", HttpMethod.POST)
                                .file(image1)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("update member profile image", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 프로필 업데이트 수행되는가")
    void updateMemberProfile() throws Exception {
        //given
        doNothing().when(memberService).updateMemberProfile(any());

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(new MemberProfileRequest("오월이", LocalDate.now(), Color.GREEN)))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString()));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("update member profile", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 회원탈퇴가 수행되는가")
    void deleteMember() throws Exception {
        //given
        doNothing().when(memberService).deleteMember();

        //when
        ResultActions perform =
                mockMvc.perform(
                        delete("/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString()));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("delete member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 감정뱃지 업데이트가 수행되는가")
    void updateEmotionalBadge() throws Exception {
        //given
        doNothing().when(memberService).updateEmotionalBadge(any());

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/emotional-badge")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(new EmotionalBadgeRequest(EmotionalBadge.HAPPY)))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString()));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("update member emotional badge", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET / home 홈화면 조회 API 테스트")
    void findHomeDate() throws Exception {
        // given
        UUID member1 = UUID.randomUUID();
        UUID member2 = UUID.randomUUID();
        List<MemberProfileResponse> membersProfile = List.of(
                new MemberProfileResponse(member1, "아빠", "http://someProfileImageUrl", EmotionalBadge.SO_HAPPY),
                new MemberProfileResponse(member2, "엄마", "http://someProfileImageUrl", EmotionalBadge.JOY),
                new MemberProfileResponse(UUID.randomUUID(), "아들","http://someProfileImageUrl",EmotionalBadge.CRY)
        );

        List<SayingByFamilyResponse> sayings = List.of(
                new SayingByFamilyResponse(UUID.randomUUID(), "오늘 회식해요", member1, List.of(UUID.randomUUID(), UUID.randomUUID()), LocalDateTime.now()),
                new SayingByFamilyResponse(UUID.randomUUID(),"오늘 저녁 카레", member2, List.of(), LocalDateTime.now())
        );
        List<ScheduleDDayResponse> dDaySchedules = List.of(
                new ScheduleDDayResponse(UUID.randomUUID(), "가족 여행", LocalDate.parse("2023-07-20"), LocalDate.parse("2023-07-23"),"D-3", ScheduleType.FAMILY, "벡스", Color.BLUE, true, List.of()),
                new ScheduleDDayResponse(UUID.randomUUID(), "휴가", LocalDate.parse("2023-07-24"), LocalDate.parse("2023-07-28"),"D-3", ScheduleType.INDIVIDUAL, "오월이", Color.SKYBLUE, true, List.of()),
                new ScheduleDDayResponse(UUID.randomUUID(), "친구 여행", LocalDate.parse("2023-07-30"), LocalDate.parse("2023-08-03"),"D-3", ScheduleType.INDIVIDUAL, "벡스", Color.GREEN, true, List.of())
                );

        MemberHomeResponse expected = new MemberHomeResponse("오월이 가족",membersProfile,dDaySchedules ,List.of("111111","222222"), sayings);

        given(memberService.findHomeData()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/home")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.family_group_name").exists());

        // docs
        perform.andDo(print())
                .andDo(document("find home data", getDocumentRequest(), getDocumentResponse()));
    }
  
    @Test
    @DisplayName("멤버의 수정가능한 색상 조회가 수행되는가")
    void getEnableColor() throws Exception {
        //given
        MemberColorResponse expected = new MemberColorResponse(false, true, false, false, false, false, false);
        when(memberService.getEnableColor()).thenReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/colors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString()));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("get member enable color", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("마이페이지 유저 조회가 제대로 수행되는가")
    void findMyPageProfile() throws Exception {
        //given
        MyPageProfileResponse expected = new MyPageProfileResponse("꼼지락", LocalDate.of(2000, 04, 22), Color.BLUE, EmotionalBadge.HAPPY, "http://someProfileImageUrl", 3L, 6L);
        given(memberService.getMyPageProfile()).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString()));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("get mypage profile", getDocumentRequest(), getDocumentResponse()));
    }
}
