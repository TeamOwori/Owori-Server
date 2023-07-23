package com.owori.domain.keyword.controller;

import com.owori.domain.keyword.dto.response.FindKeywordsResponse;
import com.owori.domain.keyword.service.KeywordService;
import com.owori.support.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Keyword 컨트롤러의")
@WebMvcTest(KeywordController.class)
public class KeywordControllerTest extends RestDocsTest {

    @MockBean private KeywordService keywordService;

    @Test
    @DisplayName("GET /keywords 최근 검색어 조회 API 테스트")
    void findAllKeyword() throws Exception {
        //given
        List<FindKeywordsResponse> expected = List.of(new FindKeywordsResponse(UUID.randomUUID(), "강화도"),
                new FindKeywordsResponse(UUID.randomUUID(), "간장 게장"),
                new FindKeywordsResponse(UUID.randomUUID(), "야구장")
        );
        given(keywordService.findSearchWords()).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/keywords")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find keywords", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("DELETE /keywords 최근 검색어 전체 삭제 API 테스트")
    void deleteAllKeyword() throws Exception {
        //given
        doNothing().when(keywordService).deleteSearchWords();

        //when
        ResultActions perform =
                mockMvc.perform(
                        delete("/keywords")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("delete keywords", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("DELETE /keywords 최근 검색어 전체 삭제 API 테스트")
    void deleteKeywordById() throws Exception {
        //given
        doNothing().when(keywordService).deleteSearchWords();

        //when
        ResultActions perform =
                mockMvc.perform(
                        delete("/keywords/{keywordId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("delete keyword", getDocumentRequest(), getDocumentResponse()));
    }

}
