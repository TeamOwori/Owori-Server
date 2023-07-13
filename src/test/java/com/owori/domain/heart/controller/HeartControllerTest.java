package com.owori.domain.heart.controller;

import com.owori.domain.heart.dto.HeartStatusResponse;
import com.owori.domain.heart.service.HeartService;
import com.owori.support.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Heart 컨트롤러의")
@WebMvcTest(HeartController.class)
public class HeartControllerTest extends RestDocsTest {

    @MockBean
    private HeartService heartService;


    @Test
    @DisplayName("POST /hearts 좋아요 등록 / 취소 API 테스트")
    void toggleHeart() throws Exception {
        //given
        HeartStatusResponse expected = new HeartStatusResponse(true);
        given(heartService.toggleHeart(any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/hearts/{storyId}",UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.is_liked").exists());

        //docs
        perform.andDo(document("toggle heart", getDocumentRequest(), getDocumentResponse()));
    }

}
