package com.owori.domain.story.controller;

import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.*;
import com.owori.domain.story.service.StoryService;
import com.owori.global.dto.IdResponse;
import com.owori.support.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Story 컨트롤러의")
@WebMvcTest(StoryController.class)
public class StoryControllerTest extends RestDocsTest{

    @MockBean private StoryService storyService;


    @Test
    @DisplayName("POST /stories 이야기 등록 API 테스트")
    void addStory() throws Exception {
        //given
        IdResponse<Long> expected = new IdResponse<>(2L);
        given(storyService.addStory(any())).willReturn(expected);

        List<UUID> imgId = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        AddStoryRequest request = new AddStoryRequest(LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), "기다리고 기다리던 하루", "종강하면 동해바다로 가족 여행 가자고 한게 엊그제 같았는데...3박 4일 동해여행 너무 재밌었어!! 날씨도 너무 좋았고 특히 갈치조림이 대박 ㄹㅇ 맛집 인정... 2일차 점심 때 대림공원 안에서 피크닉한게 가장 기억에 남았던거 같아! 엄마가 만들어 준 샌드위치는 세상에서 젤 맛있어 이거 팔면 대박날듯 ㅋㅋㅋ ",imgId);

        //when
        ResultActions perform =
                mockMvc.perform(
                                post("/stories")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toRequestBody(request))
                                        .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                        .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isCreated());

        //docs
        perform.andDo(document("save story", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories/list 이야기 리스트형 조회 API 테스트")
    void findStoryList() throws Exception {
        //given
        Long lastId = 2L;

        List<FindListStoryResponse> response = List.of(
                new FindListStoryResponse(1L,"신나는 가족여행", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "image.png", 5L, 2L, "허망고", "2022-02-01"),
                new FindListStoryResponse(2L,"다같이 보드게임 했던 날", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용 이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "image.png", 2L, 5L, "허지롱이", "2022-02-01 - 2022-02-04"));

        FindListStoryGroupResponse findListStoryGroupResponse = new FindListStoryGroupResponse(response, true);
        given(storyService.findListStory(any(),any())).willReturn(findListStoryGroupResponse);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/list")
                                .param("sort", "createAt")
                                .param("lastId", lastId.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story list", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories/album 이야기 앨범형 조회 (최신순) API 테스트")
    void findStoryAlbumByCreateAt() throws Exception {
        //given
        String size = "7";

        List<FindAlbumStoryResponse> storyResponses1 = List.of(
                new FindAlbumStoryResponse(5L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"),
                new FindAlbumStoryResponse(4L, null),
                new FindAlbumStoryResponse(3L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"),
                new FindAlbumStoryResponse(2L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"));

        List<FindAlbumStoryResponse> storyResponses2 = List.of(
                new FindAlbumStoryResponse(6L, null),
                new FindAlbumStoryResponse(1L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"));

        List<FindAlbumStoryResponse> storyResponses3 = List.of(new FindAlbumStoryResponse(10L, null));

        List<FindAlbumStoryByMonthResponse> responses = List.of(
                new FindAlbumStoryByMonthResponse("2022.08", storyResponses1),
                new FindAlbumStoryByMonthResponse("2022.07", storyResponses2),
                new FindAlbumStoryByMonthResponse("2021.04", storyResponses3)
                );

        FindAlbumStoryGroupResponse expected = new FindAlbumStoryGroupResponse(responses, true);

        given(storyService.findAlbumStory(any(),any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/album")
                                .param("sort", "createAt")
                                .param("lastViewed","2023-07-31")
                                .param("size", size)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );


        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story album by createAt", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories/album 이야기 앨범형 조회 (날짜순) API 테스트")
    void findStoryAlbumByEventAt() throws Exception {
        //given
        String size = "7";

        List<FindAlbumStoryResponse> storyResponses1 = List.of(
                new FindAlbumStoryResponse(5L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"),
                new FindAlbumStoryResponse(4L, null),
                new FindAlbumStoryResponse(3L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"),
                new FindAlbumStoryResponse(2L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"));

        List<FindAlbumStoryResponse> storyResponses2 = List.of(
                new FindAlbumStoryResponse(6L, null),
                new FindAlbumStoryResponse(1L, "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png"));

        List<FindAlbumStoryResponse> storyResponses3 = List.of(new FindAlbumStoryResponse(10L, null));

        List<FindAlbumStoryByMonthResponse> responses = List.of(
                new FindAlbumStoryByMonthResponse("2022.08", storyResponses1),
                new FindAlbumStoryByMonthResponse("2022.07", storyResponses2),
                new FindAlbumStoryByMonthResponse("2021.04", storyResponses3)
        );

        FindAlbumStoryGroupResponse expected = new FindAlbumStoryGroupResponse(responses, false);

        given(storyService.findAlbumStory(any(),any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/album")
                                .param("sort", "startDate")
                                .param("lastViewed","2023-07-31")
                                .param("size", size)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );


        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story album by eventAt", getDocumentRequest(), getDocumentResponse()));
    }


    @Test
    @DisplayName("GET /stories/{storyId} 이야기 상세 조회 API 테스트")
    void findStory() throws Exception {
        //given
        List<String> images = List.of("image2.png","image3.png","image2.png","image3.png","image3.png");

        List<CommentResponse> comments = List.of(
                new CommentResponse(null,1L,"야호 첫번째 최상위 댓글입니다.","허지렁지렁","3시간 전"),
                new CommentResponse(1L,2L,"첫번째 대댓글입니다.","김건빵","2시간 전"),
                new CommentResponse(1L,3L,"두번째 대댓글입니다.","고구마","30분 전"),
                new CommentResponse(null,4L,"야호 두번째 최상위댓글입니다.","아몬드","2시간 전")
        );

        FindStoryResponse response = new FindStoryResponse(1L,true,images, "~ 다같이 야구 보고온 날 ~", "김건빵", "오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 얏호", 5L, 4L, comments);
        given(storyService.findStory(any())).willReturn(response);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/{storyId}", 2L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );


        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story", getDocumentRequest(), getDocumentResponse()));
    }
}