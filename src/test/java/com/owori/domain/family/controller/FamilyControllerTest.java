package com.owori.domain.family.controller;

import com.owori.domain.family.dto.request.AddMemberRequest;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.service.FamilyService;
import com.owori.support.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Family 컨트롤러의")
@WebMvcTest(FamilyController.class)
class FamilyControllerTest extends RestDocsTest {
    @MockBean private FamilyService familyService;

    @Test
    @DisplayName("가족 생성이 수행되는가")
    void saveFamily() throws Exception {
        //given
        InviteCodeResponse expected = new InviteCodeResponse("oworiinvite");
        given(familyService.saveFamily(any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/families")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .content(
                                        toRequestBody(new FamilyRequest("우리가족"))));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.invite_code").exists());

        //docs
        perform.andDo(print())
                .andDo(document("save family", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("가족 인증을 통한 멤버 추가가 수행되는가")
    void saveFamilyMember() throws Exception {
        //given
        doNothing().when(familyService).addMember(any());

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/families/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .content(
                                        toRequestBody(new AddMemberRequest("우리가족"))));

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("add member to family", getDocumentRequest(), getDocumentResponse()));
    }
}
