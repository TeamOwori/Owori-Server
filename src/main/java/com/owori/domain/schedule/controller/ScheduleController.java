package com.owori.domain.schedule.controller;

import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.service.ScheduleService;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<IdResponse<UUID>> addSchedule(@RequestBody AddScheduleRequest addScheduleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.addSchedule(addScheduleRequest));
    }

    /**
     * 일정 수정 컨트롤러입니다.
     * 수정된 일정 정보(제목, 시작일, 종료일, 타입, 디데이 여부, 알람 옵션)을 입력받고 기존 일정을 수정합니다.
     * @param scheduleId 수정할 일정의 id값입니다.
     * @param updateScheduleRequest 수정된 일정의 정보입니다.
     * @return UpdateScheduleResponse는 수정된 일정의 id값입니다.
     */
    @PatchMapping("/update")
    public ResponseEntity<IdResponse<UUID>> updateSchedule(@RequestParam UUID scheduleId, @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, updateScheduleRequest));
    }

    /**
     * 월별 일정 조회 컨트롤러입니다.
     * 조회할 달을 입력받고 해당 달의 일정을 조회합니다.
     * @param yearMonth  'yy-MM'형태로 조회할 달에 대한 정보입니다.
     * @return List<FindScheduleByMonthResponse> 해당 달에 대한 일정 리스트 정보입니다.
     */
    @GetMapping("/month")
    public ResponseEntity<List<FindScheduleByMonthResponse>> findScheduleByMonth(@RequestParam String yearMonth) {
        return ResponseEntity.ok(scheduleService.findScheduleByMonth(yearMonth));
    }
}
