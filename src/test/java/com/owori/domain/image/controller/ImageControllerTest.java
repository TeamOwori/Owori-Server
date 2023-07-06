package com.owori.domain.image.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@DisplayName("Image 컨트롤러의")
@WebMvcTest(ImageController.class)
public class ImageControllerTest extends RestDocsTest{

    @MockBean private ImageService imageService;

    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /images 이야기 이미지 업로드 API 테스트")
    void addStoryImage() throws Exception {
        //given
        List<UUID> expected = List.of(UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID());
        given(imageService.addStoryImage(any())).willReturn(expected);

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "Image 1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "Image 2".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile metadata = new MockMultipartFile("metadata", "", "application/json", "{ \"version\": \"1.0\"}".getBytes());

        //when
        ResultActions perform =
                mockMvc.perform(
                                multipart("/images", HttpMethod.POST)
                                        .file(image1).file(image2).file(metadata)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                );

        //then
        perform.andExpect(status().isCreated());

        //docs
        perform.andDo(document("save story image", getDocumentRequest(), getDocumentResponse()));
    }


}