package com.owori.domain.member.controller;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.request.MemberDetailsRequest;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.service.MemberService;
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
    @DisplayName("멤버 생성 후 JwtToken 생성이 수행되는가")
    void saveMember() throws Exception {
        //given
        JwtToken jwt = new JwtToken("accesasdfagfwaerg.tokenasfd13sad.isthisahtfgwiueoh", "refreshriuqwhfoieu.tokenqiweurhu.isthiswheoituhw");
        MemberJwtResponse expected = new MemberJwtResponse(UUID.randomUUID(), jwt);
        when(memberService.saveIfNone(any())).thenReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new MemberRequest("382y5e3a5", AuthProvider.KAKAO))));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt_token.access_token").exists())
                .andExpect(jsonPath("$.jwt_token.refresh_token").exists())
                .andExpect(jsonPath("$.member_id").exists());

        //docs
        perform.andDo(print())
                .andDo(document("save member and get jwt", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 기본 정보 업데이트가 수행되는가")
    void updateMemberDetails() throws Exception {
        //given
        doNothing().when(memberService).updateMemberDetails(any());

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new MemberDetailsRequest("owori", LocalDate.now())))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString()));

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
        doNothing().when(memberService).updateMemberProfileImage(any());
        MockMultipartFile image1 = new MockMultipartFile("profile_image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "Image".getBytes(StandardCharsets.UTF_8));

        //when
        ResultActions perform =
                mockMvc.perform(
                        multipart("/members/profile-image", HttpMethod.POST)
                                .file(image1)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("update member profile image", getDocumentRequest(), getDocumentResponse()));
    }
}
