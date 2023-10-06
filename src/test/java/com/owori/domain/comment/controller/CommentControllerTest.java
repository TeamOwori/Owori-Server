package com.owori.domain.comment.controller;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.dto.response.CommentIdResponse;
import com.owori.domain.comment.service.CommentService;
import com.owori.domain.story.service.FacadeService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@DisplayName("Comment 컨트롤러의")
@WebMvcTest(CommentController.class)
public class CommentControllerTest extends RestDocsTest {

    @MockBean private CommentService commentService;
    @MockBean private FacadeService facadeService;

    @Test
    @DisplayName("POST /comments 댓글 작성 테스트")
    void addComment() throws Exception {
        //given
        CommentIdResponse expected = new CommentIdResponse(UUID.randomUUID());
        given(facadeService.addComment(any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(new AddCommentRequest(UUID.randomUUID(), null, "여기엔 댓글 내용을 적어주시면 됩니다. 작성하는 댓글이 최상위 댓글인 경우에는 parentCommentId에 null을 담아주세요 >_<")))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isCreated());

        //docs
        perform.andDo(document("add comment", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                        fieldWithPath("story_id").description("댓글을 작성할 story의 id"),
                        fieldWithPath("parent_comment_id").description("부모 댓글의 id (null도 가능)").optional(),
                        fieldWithPath("content").description("댓글 내용")
                ),
                responseFields(
                        fieldWithPath("comment_id").description("댓글의 id")
                )));
    }

    @Test
    @DisplayName("DELETE /comments 댓글 삭제 테스트")
    void deleteComment() throws Exception {
        //given
        doNothing().when(commentService).removeComment(any());

        //when
        ResultActions perform =
                mockMvc.perform(
                        delete("/comments/{commentId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(document("delete comment", getDocumentRequest(), getDocumentResponse(),
                pathParameters(
                        parameterWithName("commentId").description("삭제할 댓글의 id")
                )));
    }

    @Test
    @DisplayName("POST /comments/{commentId} 댓글 수정 테스트")
    void updateComment() throws Exception {
        //given
        UUID commentId = UUID.randomUUID();
        CommentIdResponse expected = new CommentIdResponse(commentId);
        given(commentService.updateComment(any())).willReturn(expected);

        //when
        ResultActions perform =
                mockMvc.perform(
                        post("/comments/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(new UpdateCommentRequest(commentId,  "수정할 댓글 내용")))
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("member_id", UUID.randomUUID().toString())
                );

        //then
        perform.andExpect(status().isOk());

        //docs
        perform.andDo(document("update comment", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                        fieldWithPath("comment_id").description("수정할 댓글의 id"),
                        fieldWithPath("comment").description("수정할 댓글 내용")
                )));
    }
}
