package com.owori.domain.image.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owori.domain.image.dto.response.ImagesStoryResponse;
import com.owori.domain.image.service.ImageService;
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
import java.util.List;
import java.util.UUID;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;


@DisplayName("Image 컨트롤러의")
@WebMvcTest(ImageController.class)
public class ImageControllerTest extends RestDocsTest{

    @MockBean private ImageService imageService;

    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /images 이야기 이미지 업로드 API 테스트")
    void addStoryImage() throws Exception {
        //given
        ImagesStoryResponse expected = new ImagesStoryResponse(List.of("http://storyImage1Url","http://storyImage2Url"));
        given(imageService.addStoryImage(any())).willReturn(expected);

        MockMultipartFile image1 = new MockMultipartFile("story_images", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "Image 1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image2 = new MockMultipartFile("story_images", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "Image 2".getBytes(StandardCharsets.UTF_8));

        //when
        ResultActions perform =
                mockMvc.perform(
                                multipart("/images", HttpMethod.POST)
                                        .file(image1).file(image2)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                        .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isCreated());

        //docs
        perform.andDo(document("save story image", getDocumentRequest(), getDocumentResponse(),
                requestParts(
                        partWithName("story_images").description("업로드 할 이미지 파일")
                ),
                responseFields(
                        fieldWithPath("story_images").description("이미지에 접근 가능한 url")
                )));
    }


}