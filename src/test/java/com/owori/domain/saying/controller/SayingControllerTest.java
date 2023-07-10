package com.owori.domain.saying.controller;

import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.service.SayingService;
import com.owori.global.dto.IdResponse;
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

import java.util.List;
import java.util.UUID;

@DisplayName("Saying 컨트롤러의")
@WebMvcTest(SayingController.class)
public class SayingControllerTest extends RestDocsTest {
    @MockBean private SayingService sayingService;

    @Test
    @DisplayName("POST / saying 서로에게 한마디 등록 API 테스트")
    void addSaying() throws Exception {
        // given
        IdResponse<UUID> expected = new IdResponse<UUID>(UUID.randomUUID());
        given(sayingService.addSaying(any())).willReturn(expected);

        AddSayingRequest request = new AddSayingRequest("오늘 집 안 들어가요", List.of());

        // when
        ResultActions perfrom =
                mockMvc.perform(
                        post("/saying")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                                .content(toRequestBody(request))
                );

        // then
        perfrom.andExpect(status().isCreated());

        // docs
        perfrom.andDo(document("save saying", getDocumentRequest(), getDocumentResponse()));

    }
}
