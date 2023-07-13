package com.owori.domain.comment.controller;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.service.CommentService;
import com.owori.global.dto.IdResponse;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Heart 컨트롤러의")
@WebMvcTest(CommentController.class)
public class CommentControllerTest extends RestDocsTest {

    @MockBean private CommentService commentService;

    @Test
    @DisplayName("POST /comments 댓글 작성 테스트")
    void addComment() throws Exception {
        //given
        IdResponse<UUID> expected = new IdResponse<>(UUID.randomUUID());
        given(commentService.addComment(any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new AddCommentRequest(UUID.randomUUID(), null, "여기엔 댓글 내용을 적어주시면 됩니다. 작성하는 댓글이 최상위 댓글인 경우에는 parentCommentId에 null을 담아주세요 >_<")))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        //docs
        perform.andDo(document("add comment", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("DELETE /comments 댓글 삭제 테스트")
    void deleteComment() throws Exception {
        //given
        doNothing().when(commentService).removeComment(any());

        //when
        ResultActions perform =
                mockMvc.perform(
                        delete("/comments/{commentId}",UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(document("delete comment", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("POST /comments/{commentId} 댓글 수정 테스트")
    void updateComment() throws Exception {
        //given
        IdResponse<UUID> expected = new IdResponse<>(UUID.randomUUID());
        given(commentService.updateComment(any(),any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/comments/{commentId}",UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new UpdateCommentRequest("수정할 댓글 내용")))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        //docs
        perform.andDo(document("update comment", getDocumentRequest(), getDocumentResponse()));
    }
}
