package com.owori.domain.member.controller;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.service.AuthService;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Auth 컨트롤러의")
@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocsTest {

    @MockBean private AuthService authService;

    @Test
    @DisplayName("리프레시 검증 및 재발급 기능이 수행되는가")
    void authorize() throws Exception {
        //given
        JwtToken expected = JwtToken.builder().accessToken("asdfiuer3oidwf12.asdfoihoihn23vs.grwoi6ghrweiog5h").refreshToken("ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb").build();
        given(authService.refreshToken(any(), any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("refreshToken", "ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("accessToken", "asdfiuer3oidwf12.asdfoihoihn23vs.grwoi6ghrweiog5h"));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());

        //docs
        perform.andDo(print())
                .andDo(document("refresh jwt token", getDocumentRequest(), getDocumentResponse()));
    }
}
