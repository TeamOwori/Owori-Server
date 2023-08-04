package com.owori.domain.schedule.controller;

import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.ScheduleByMonthResponse;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import com.owori.domain.schedule.dto.response.ScheduleIdResponse;
import com.owori.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
     * 일정 추가 컨트롤러입니다.
     * 일정 정보(제목, 시작일, 종료일, 타입, 디데이 여부, 알람 옵션)을 입력받고 새로운 일정을 추가합니다.
     * @param addScheduleRequest 추가된 일정의 정보입니다.
     * @return AddScheduleResponse는 추가된 일정의 id값입니다.
     */
    @PostMapping
    public ResponseEntity<ScheduleIdResponse> addSchedule(@Valid @RequestBody AddScheduleRequest addScheduleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.addSchedule(addScheduleRequest));
    }

    /**
     * 일정 수정 컨트롤러입니다.
     * 수정된 일정 정보(제목, 시작일, 종료일, 타입, 디데이 여부, 알람 옵션)을 입력받고 기존 일정을 수정합니다.
     * @param updateScheduleRequest 수정된 일정의 정보입니다.
     * @return UpdateScheduleResponse는 수정된 일정의 id값입니다.
     */
    @PostMapping("/update")
    public ResponseEntity<ScheduleIdResponse> updateSchedule(
            @Valid @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        return ResponseEntity.ok(scheduleService.updateSchedule(updateScheduleRequest));
    }

    /**
     * 월별 일정 조회 컨트롤러입니다.
     * 조회할 달을 입력받고 해당 달의 일정을 조회합니다.
     * @param yearMonth 'yy-MM'형태로 조회할 달에 대한 정보입니다.
     * @return List<FindScheduleByMonthResponse> 해당 달에 대한 일정 리스트 정보입니다.
     */
    @GetMapping("/month")
    public ResponseEntity<List<ScheduleByMonthResponse>> findScheduleByMonth(
            @RequestParam("year_month") @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])", message = "\"yyyy-MM\"형태로 입력하세요") String yearMonth) {
        return ResponseEntity.ok(scheduleService.findScheduleByMonth(yearMonth));
    }

    /**
     * 일정 삭제 컨트롤러입니다.
     * @param scheduleId 삭제할 일정 id 입니다.
     * @return Void 반환
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 가족단위 디데이 조회 컨트롤러입니다.
     * @return 디데이 옵션이 켜진 일정 반환
     */

    @GetMapping("/dday")
    public ResponseEntity<List<ScheduleDDayResponse>> findDDayByFamily() {
        return ResponseEntity.ok(scheduleService.findDDayByFamily());
    }
}
