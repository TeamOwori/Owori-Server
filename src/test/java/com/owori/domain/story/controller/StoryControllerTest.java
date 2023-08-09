package com.owori.domain.story.controller;

import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.story.dto.request.PostStoryRequest;
import com.owori.domain.story.dto.request.UpdateStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryGroupResponse;
import com.owori.domain.story.dto.response.FindAllStoryResponse;
import com.owori.domain.story.dto.response.FindStoryResponse;
import com.owori.domain.story.dto.response.StoryIdResponse;
import com.owori.domain.story.service.FacadeService;
import com.owori.domain.story.service.StoryService;
import com.owori.global.dto.ImageResponse;
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
import java.util.stream.Stream;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Story 컨트롤러의")
@WebMvcTest(StoryController.class)
class StoryControllerTest extends RestDocsTest{

    @MockBean private StoryService storyService;
    @MockBean private FacadeService facadeService;

    @Test
    @DisplayName("POST /stories 이야기 등록 API 테스트")
    void addStory() throws Exception {
        //given
        StoryIdResponse expected = new StoryIdResponse(UUID.randomUUID());
        given(storyService.addStory(any())).willReturn(expected);

        List<String> imgsUrl = List.of("http://storyImage1Url","http://storyImage2Url", "http://storyImage3Url", "http://storyImage4Url");
        PostStoryRequest request = new PostStoryRequest(LocalDate.parse("2017-12-25"), LocalDate.parse("2017-12-30"), "기다리고 기다리던 하루", "종강하면 동해바다로 가족 여행 가자고 한게 엊그제 같았는데...3박 4일 동해여행 너무 재밌었어!! 날씨도 너무 좋았고 특히 갈치조림이 대박 ㄹㅇ 맛집 인정... 2일차 점심 때 대림공원 안에서 피크닉한게 가장 기억에 남았던거 같아! 엄마가 만들어 준 샌드위치는 세상에서 젤 맛있어 이거 팔면 대박날듯 ㅋㅋㅋ ", imgsUrl);

        //when
        ResultActions perform =
                mockMvc.perform(
                                post("/stories")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toRequestBody(request))
                                        .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                        .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isCreated());

        //docs
        perform.andDo(document("save story", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("POST /stories/{storyId} 이야기 수정 API 테스트")
    void updateStory() throws Exception {
        //given
        StoryIdResponse expected = new StoryIdResponse(UUID.randomUUID());
        given(storyService.updateStory(any())).willReturn(expected);
        List<String> imgsUrl = List.of("http://storyImage1Url","http://storyImage2Url", "http://storyImage3Url", "http://storyImage4Url");
        UpdateStoryRequest request = new UpdateStoryRequest(UUID.randomUUID(), LocalDate.parse("2012-12-25"), LocalDate.parse("2012-12-25"), "기다리고 기다리던 하루", "종강하면 동해바다로 가족 여행 가자고 한게 엊그제 같았는데...3박 4일 동해여행 너무 재밌었어!! 날씨도 너무 좋았고 특히 갈치조림이 대박 ㄹㅇ 맛집 인정... 2일차 점심 때 대림공원 안에서 피크닉한게 가장 기억에 남았던거 같아! 엄마가 만들어 준 샌드위치는 세상에서 젤 맛있어 이거 팔면 대박날듯 ㅋㅋㅋ ", imgsUrl);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/stories/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(request))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(document("update story", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories 이야기 전체 조회 (날짜순) API 테스트")
    void findAllStoryByEventAt() throws Exception {
        //given
        List<FindAllStoryResponse> response = List.of(
                new FindAllStoryResponse(UUID.randomUUID(),"신나는 가족여행", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 2, "고구마", LocalDate.of(2002, 02, 01), LocalDate.of(2002, 02, 02)),
                new FindAllStoryResponse(UUID.randomUUID(),"맛있는 저녁식사", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", null, 0, 0, "구운계란", LocalDate.of(2005, 02, 01), LocalDate.of(2005, 02, 03)),
                new FindAllStoryResponse(UUID.randomUUID(),"못난이 생일잔치", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 1, 0, "허망고", LocalDate.of(2012, 02, 01), LocalDate.of(2012, 02, 02)),
                new FindAllStoryResponse(UUID.randomUUID(),"다같이 보드게임 했던 날", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용 이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 3, "허지롱이", LocalDate.of(2022, 02, 01), LocalDate.of(2022, 12, 03)));

        FindAllStoryGroupResponse findAllStoryGroupResponse = new FindAllStoryGroupResponse(response, true);
        given(storyService.findAllStory(any(),any())).willReturn(findAllStoryGroupResponse);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories")
                                .param("sort", "start_date")
                                .param("last_viewed","2022-03-31")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story by eventAt", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories 이야기 전체 조회 (최신순) API 테스트")
    void findAllStoryByCreatedAt() throws Exception {
        //given
        List<FindAllStoryResponse> response = List.of(
                new FindAllStoryResponse(UUID.randomUUID(),"다같이 보드게임 했던 날", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용 이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 3, "허지롱이", LocalDate.of(2022, 02, 01), LocalDate.of(2022, 12, 03)),
                new FindAllStoryResponse(UUID.randomUUID(),"못난이 생일잔치", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 1, 0, "허망고", LocalDate.of(2012, 02, 01), LocalDate.of(2012, 02, 02)),
                new FindAllStoryResponse(UUID.randomUUID(),"맛있는 저녁식사", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", null, 0, 0, "구운계란", LocalDate.of(2005, 02, 01), LocalDate.of(2005, 02, 03)),
                new FindAllStoryResponse(UUID.randomUUID(),"신나는 가족여행", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 2, "고구마", LocalDate.of(2002, 02, 01), LocalDate.of(2002, 02, 02)));

        FindAllStoryGroupResponse findAllStoryGroupResponse = new FindAllStoryGroupResponse(response, true);
        given(storyService.findAllStory(any(),any())).willReturn(findAllStoryGroupResponse);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories")
                                .param("sort", "created_at")
                                .param("last_viewed","2023-08-31")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story by createdAt", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories/{storyId} 이야기 상세 조회 API 테스트")
    void findStory() throws Exception {
        //given
        List<String> images = List.of("image2.png","image3.png","image2.png","image3.png","image3.png");

        List<CommentResponse> comments = List.of(
                new CommentResponse(null, UUID.fromString("6da8a00c-b1df-4496-a8d7-6323294ba0ef"),"야호 첫번째 최상위 댓글입니다.","허지렁지렁","3시간 전", Boolean.FALSE),
                new CommentResponse(UUID.fromString("6da8a00c-b1df-4496-a8d7-6323294ba0ef"), UUID.randomUUID(),"첫번째 대댓글입니다.","김건빵","2시간 전", Boolean.FALSE),
                new CommentResponse(UUID.fromString("6da8a00c-b1df-4496-a8d7-6323294ba0ef"), UUID.randomUUID(),"두번째 대댓글입니다.","고구마","30분 전", Boolean.FALSE),
                new CommentResponse(null, UUID.randomUUID(),"야호 두번째 최상위댓글입니다.","아몬드","2시간 전", Boolean.FALSE)
        );

        FindStoryResponse response = new FindStoryResponse(UUID.randomUUID(),true, images, "~ 다같이 야구 보고온 날 ~", "김건빵", "오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 오늘은 엘지가 이겼다. 얏호", 5, 4, comments, LocalDate.now(), LocalDate.now());
        given(facadeService.findStory(any())).willReturn(response);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/{storyId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );


        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("DELETE /stories/{storyId} 이야기 삭제 API 테스트")
    void removeStory() throws Exception {
        //given
        doNothing().when(facadeService).removeStory(any());

        //when
        ResultActions perform =
                mockMvc.perform(
                        delete("/stories/{storyId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("remove story", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories 검색 API 테스트")
    void findStoryBySearch() throws Exception {
        //given
        List<FindAllStoryResponse> response = List.of(
                new FindAllStoryResponse(UUID.randomUUID(),"룰루랄라", "이야기 내용입니다 못난이 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 2, "고구마", LocalDate.of(2022, 02, 01), LocalDate.of(2022, 02, 02)),
                new FindAllStoryResponse(UUID.randomUUID(),"못난이 외식 했지롱", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", null, 0, 0, "구운계란", LocalDate.of(2005, 02, 01), LocalDate.of(2019, 02, 03)),
                new FindAllStoryResponse(UUID.randomUUID(),"생일잔치", "못난이 이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 1, 0, "허망고", LocalDate.of(2011, 02, 01), LocalDate.of(2011, 02, 02)),
                new FindAllStoryResponse(UUID.randomUUID(),"쇼핑 데이 with 못난이", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용 이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 3, "못난이", LocalDate.of(2010, 02, 01), LocalDate.of(2022, 12, 03)));

        FindAllStoryGroupResponse findAllStoryGroupResponse = new FindAllStoryGroupResponse(response, false);
        given(storyService.findStoryBySearch(any(),any(), any())).willReturn(findAllStoryGroupResponse);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/search")
                                .param("keyword", "못난이")
                                .param("last_viewed","2023-08-31")
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                                .characterEncoding("UTF-8")
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story by search", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories/member 유저가 작성한 이야기 조회 API 테스트")
    void findStoryByMember() throws Exception {
        //given
        List<FindAllStoryResponse> response = List.of(
                new FindAllStoryResponse(UUID.randomUUID(),"룰루랄라", "이야기 내용입니다 못난이 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 2, "고구마", LocalDate.of(2022, 02, 01), LocalDate.of(2022, 02, 02)),
                new FindAllStoryResponse(UUID.randomUUID(),"못난이 외식 했지롱", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", null, 0, 0, "구운계란", LocalDate.of(2005, 02, 01), LocalDate.of(2019, 02, 03)));
        FindAllStoryGroupResponse findAllStoryGroupResponse = new FindAllStoryGroupResponse(response, false);
        given(storyService.findStoryByWriter(any(),any())).willReturn(findAllStoryGroupResponse);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/member")
                                .param("sort", "start_date")
                                .param("last_viewed","2023-08-31")
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story by member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET /stories/heart 유저가 좋아한 이야기 조회 API 테스트")
    void findStoryByHeart() throws Exception {
        //given
        List<FindAllStoryResponse> response = List.of(
                new FindAllStoryResponse(UUID.randomUUID(),"선풍기 청소한 날", "이야기 내용입니다 못난이 내용 내용 내용 내용 내용 내용 내용 내용 내용", "https://owori.s3.ap-northeast-2.amazonaws.com/story/Group%2010_f985a58a-1257-4691-88ee-e2b75977fb3e.png", 2, 2, "고구마", LocalDate.of(2022, 02, 01), LocalDate.of(2022, 02, 02)),
                new FindAllStoryResponse(UUID.randomUUID(),"못난이 외식 했지롱", "이야기 내용입니다 내용 내용 내용 내용 내용 내용 내용 내용 내용", null, 0, 0, "구운계란", LocalDate.of(2005, 02, 01), LocalDate.of(2019, 02, 03))
        );
        FindAllStoryGroupResponse findAllStoryGroupResponse = new FindAllStoryGroupResponse(response, true);
        given(storyService.findStoryByHeart(any(),any())).willReturn(findAllStoryGroupResponse);

        //when
        ResultActions perform =
                mockMvc.perform(
                        get("/stories/heart")
                                .param("sort", "created_at")
                                .param("last_viewed","2023-08-31")
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(print())
                .andDo(document("find story by heart", getDocumentRequest(), getDocumentResponse()));
    }
}