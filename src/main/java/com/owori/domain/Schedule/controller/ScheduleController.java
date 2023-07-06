package com.owori.domain.Schedule.controller;

import com.owori.domain.Schedule.dto.request.AddScheduleRequest;
import com.owori.domain.Schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.Schedule.dto.response.AddScheduleResponse;
import com.owori.domain.Schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.Schedule.dto.response.UpdateScheduleResponse;
import com.owori.domain.Schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<AddScheduleResponse> addSchedule(@RequestBody AddScheduleRequest addScheduleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.addSchedule(addScheduleRequest));
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(@RequestParam UUID id, @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        return ResponseEntity.ok(scheduleService.updateSchedule(updateScheduleRequest));
    }

    @GetMapping("/month")
    public ResponseEntity<List<FindScheduleByMonthResponse>> findScheduleByMonth(@PageableDefault(sort = "startDate", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String yearMonth) {
        return ResponseEntity.ok(scheduleService.findScheduleByMonth(pageable, yearMonth));
    }
}
