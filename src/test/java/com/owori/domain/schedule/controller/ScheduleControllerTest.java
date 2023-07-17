package com.owori.domain.schedule.controller;

import com.owori.domain.member.entity.Color;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import com.owori.domain.schedule.dto.response.ScheduleByMonthResponse;
import com.owori.domain.schedule.entity.Alarm;
import com.owori.domain.schedule.entity.ScheduleType;
import com.owori.domain.schedule.service.ScheduleService;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Schedule 컨트롤러의")
@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest extends RestDocsTest {
    @MockBean private ScheduleService scheduleService;

    @Test
    @DisplayName("POST / schedule 일정 등록 API 테스트")
        void addSchedule() throws Exception {
        // given
        IdResponse<UUID> expected = new IdResponse<UUID>(UUID.randomUUID());
        given(scheduleService.addSchedule(any())).willReturn(expected);

        AddScheduleRequest request = new AddScheduleRequest("가족 여행", LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), ScheduleType.FAMILY, true, List.of(Alarm.TODAY, Alarm.A_DAY_AGO));

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                                .content(toRequestBody(request))
                );

        // then
        perform.andExpect(status().isCreated());

        // docs
        perform.andDo(document("save schedule", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("POST / schedule 일정 수정 API 테스트")
    void updateSchedule() throws Exception {
        // given
        IdResponse<UUID> expected = new IdResponse<UUID>(UUID.randomUUID());
        given(scheduleService.updateSchedule(any(), any())).willReturn(expected);

        UpdateScheduleRequest request = new UpdateScheduleRequest("가족 여행", LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-04"), true, List.of());

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/schedule/update")
                                .param("scheduleId", UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                                .content(toRequestBody(request))
                );

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(document("update schedule", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("DELETE / schedule 일정 삭제 API 테스트")
    void deleteSchedule() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        doNothing().when(scheduleService).deleteSchedule(any());

        // when
        ResultActions perform =
                mockMvc.perform(
                        delete("/schedule")
                                .param("scheduleId", id.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization","Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(document("delete schedule", getDocumentRequest(),getDocumentResponse()));
    }

    @Test
    @DisplayName("GET / schedule 일정 월별 조회 API 테스트")
    void findScheduleByMonth() throws Exception {
        // given
        List<ScheduleByMonthResponse> expected = List.of(
                new ScheduleByMonthResponse(UUID.randomUUID(),"친구랑 여행", LocalDate.parse("2023-07-08"), LocalDate.parse("2023-07-09"), ScheduleType.INDIVIDUAL, "벡스", Color.BLUE, true, List.of(Alarm.TODAY)),
                new ScheduleByMonthResponse(UUID.randomUUID(),"코딩 테스트", LocalDate.parse("2023-07-15"), LocalDate.parse("2023-07-15"), ScheduleType.INDIVIDUAL , "오월이", Color.BLUE, true, List.of(Alarm.A_DAY_AGO)),
                new ScheduleByMonthResponse(UUID.randomUUID(),"가족여행",LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), ScheduleType.FAMILY, "가족", Color.BLUE, true, List.of(Alarm.A_DAY_AGO, Alarm.A_WEEK_AGO))
        );

        given(scheduleService.findScheduleByMonth(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/schedule/month")
                                .param("yearMonth","2023-07")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].start_date").exists());

        // docs
        perform.andDo(print())
                .andDo(document("find schedules by month", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("GET / schedule 가족별 디데이 일정 조회 API 테스트")
    void findDDayByFamily() throws Exception {
        // given
        List<ScheduleDDayResponse> expected = List.of(
                new ScheduleDDayResponse(UUID.randomUUID(),"친구랑 여행", LocalDate.parse("2023-07-16"), LocalDate.parse("2023-07-09"),"D-DAY", ScheduleType.INDIVIDUAL, "벡스", Color.BLUE, true, List.of(Alarm.TODAY)),
                new ScheduleDDayResponse(UUID.randomUUID(),"코딩 테스트", LocalDate.parse("2023-07-17"), LocalDate.parse("2023-07-15"), "D-1", ScheduleType.INDIVIDUAL , "오월이", Color.BLUE, true, List.of(Alarm.A_DAY_AGO)),
                new ScheduleDDayResponse(UUID.randomUUID(),"가족여행",LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"),"D-15", ScheduleType.FAMILY, "벡스", Color.BLUE, true, List.of(Alarm.A_DAY_AGO, Alarm.A_WEEK_AGO))
        );

        given(scheduleService.findDDayByFamily()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/schedule/dday")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].start_date").exists());

        // docs
        perform.andDo(print())
                .andDo(document("find schedules by family", getDocumentRequest(), getDocumentResponse()));
    }
}
