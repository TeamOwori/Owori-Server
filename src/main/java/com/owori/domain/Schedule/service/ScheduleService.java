package com.owori.domain.Schedule.service;

import com.owori.domain.Schedule.dto.request.AddScheduleRequest;
import com.owori.domain.Schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.Schedule.dto.response.AddScheduleResponse;
import com.owori.domain.Schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.Schedule.dto.response.UpdateScheduleResponse;
import com.owori.domain.Schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public AddScheduleResponse addSchedule(AddScheduleRequest addScheduleRequest) {
        return null; // todo 로직 작성
    }

    public UpdateScheduleResponse updateSchedule(UpdateScheduleRequest updateScheduleRequest) {
        return null; // todo 로직작성
    }

    public List<FindScheduleByMonthResponse> findScheduleByMonth(Pageable pageable, String month) {
        return null; // todo 로직작성
    }
}
