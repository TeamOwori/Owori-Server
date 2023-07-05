package com.owori.domain.story.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.AddStoryResponse;
import com.owori.domain.story.service.StoryService;
import com.owori.support.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartBody;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Story 컨트롤러의")
@WebMvcTest(StoryController.class)
public class StoryControllerTest extends RestDocsTest{

    @MockBean private StoryService storyService;
    
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /stories 이야기 등록 API 테스트")
    void saveStory() throws Exception {
        //given
        AddStoryResponse expected = new AddStoryResponse("12345678-1234-5678-1234-567812345678");
        given(storyService.saveStory(any(),any())).willReturn(expected);

        AddStoryRequest request = new AddStoryRequest(LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), "기다리고 기다리던 하루", "종강하면 동해바다로 가족 여행 가자고 한게 엊그제 같았는데...3박 4일 동해여행 너무 재밌었어!! 날씨도 너무 좋았고 특히 갈치조림이 대박 ㄹㅇ 맛집 인정... 2일차 점심 때 대림공원 안에서 피크닉한게 가장 기억에 남았던거 같아! 엄마가 만들어 준 샌드위치는 세상에서 젤 맛있어 이거 팔면 대박날듯 ㅋㅋㅋ ");
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "Image 1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "Image 2".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile metadata = new MockMultipartFile("metadata", "", "application/json", "{ \"version\": \"1.0\"}".getBytes());

        //when
        ResultActions perform =
                mockMvc.perform(
                                multipart("/stories", HttpMethod.POST)
                                        .file(image1).file(image2).file(metadata)
                                        .content(objectMapper.writeValueAsString(request))
                                        .accept(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                );

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.story_id").exists());

        //docs
        perform.andDo(document("save story", getDocumentRequest(), getDocumentResponse(), requestPartBody("images")));

}}
