package com.owori.domain.schedule.service;

import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.AddScheduleResponse;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.dto.response.UpdateScheduleResponse;
import com.owori.domain.schedule.repository.ScheduleRepository;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public IdResponse addSchedule(AddScheduleRequest addScheduleRequest) {
        return null; // todo 로직 작성
    }

    public IdResponse updateSchedule(UUID scheduleId, UpdateScheduleRequest updateScheduleRequest) {
        return null; // todo 로직작성
    }

    public List<FindScheduleByMonthResponse> findScheduleByMonth(Pageable pageable, String month) {
        return null; // todo 로직작성
    }
}
