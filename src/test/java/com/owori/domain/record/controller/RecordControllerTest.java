package com.owori.domain.record.controller;

import com.owori.domain.member.entity.Color;
import com.owori.domain.record.dto.request.AddRecordRequest;
import com.owori.domain.record.dto.request.UpdateRecordRequest;
import com.owori.domain.record.dto.response.AddRecordResponse;
import com.owori.domain.record.dto.response.FindRecordByMonthResponse;
import com.owori.domain.record.dto.response.UpdateRecordResponse;
import com.owori.domain.record.service.RecordService;
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


@DisplayName("Record 컨트롤러의")
@WebMvcTest(RecordController.class)
public class RecordControllerTest extends RestDocsTest {
    @MockBean private RecordService recordService;

    @Test
    @DisplayName("POST / record 일정 등록 API 테스트")
    void addRecord() throws Exception {
        // given
        AddRecordResponse expected = new AddRecordResponse(UUID.randomUUID());
        given(recordService.addRecord(any()));

        AddRecordRequest request = new AddRecordRequest("가족 여행", LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), "가족", true, List.of("당일", "하루전"));

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/record")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb") // ??
                                .content(toRequestBody(request))
                );

        // then
        perform.andExpect(status().isCreated());

        // docs
        perform.andDo(document("save record", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("PATCH / record 일정 수정 API 테스트")
    void updateRecord() throws Exception {
        // given
        UpdateRecordResponse expected = new UpdateRecordResponse(UUID.randomUUID());
        given(recordService.updateRecord(any()));

        UpdateRecordRequest request = new UpdateRecordRequest("가족 여행", LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-04"), "가족", true, List.of("당일"));

        // when
        ResultActions perform =
                mockMvc.perform(
                        patch("/record/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb") // ??
                                .content(toRequestBody(request))
                );

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(document("update record", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET / record 일정 월별 조회 API 테스트")
    void findRecordByMonth() throws Exception {
        // given
        List<FindRecordByMonthResponse> expected = List.of(
                new FindRecordByMonthResponse("친구랑 여행", LocalDate.parse("2023-07-08"), LocalDate.parse("2023-07-09"), Color.BLUE, true, List.of("당일")),
                new FindRecordByMonthResponse("코딩 테스트", LocalDate.parse("2023-07-15"), LocalDate.parse("2023-07-15"), Color.BLUE, true, List.of("당일")),
                new FindRecordByMonthResponse("가족여행",LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), Color.ORANGE, true, List.of("하루전","일주일전"))
        );

        given(recordService.findRecordByMonth(any(), any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        patch("/record/update")
                                .param("sort","startDate")
                                .param("month","07")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb") // ??
                );

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].start_date").exists());

        // docs
        perform.andDo(print())
                .andDo(document("find record by month", getDocumentRequest(), getDocumentResponse()));
    }
}
