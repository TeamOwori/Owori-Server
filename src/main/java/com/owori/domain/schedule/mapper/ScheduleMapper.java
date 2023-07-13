package com.owori.domain.schedule.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.entity.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleMapper {
    public Schedule toEntity(AddScheduleRequest request, Member member) {
        return Schedule.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .scheduleType(request.getScheduleType())
                .dDayOption(request.getDDayOption())
                .alarmList(request.getAlarmOptions())
                .member(member)
                .build();
    }

    public List<FindScheduleByMonthResponse> toResponseList(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::toResponse)
                .toList();
    }

    private FindScheduleByMonthResponse toResponse(Schedule schedule) {
        return FindScheduleByMonthResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .memberId(schedule.getMember().getId())
                .color(schedule.getMember().getColor())
                .dDayOption(schedule.getDDayOption())
                .alarmOptions(schedule.getAlarmList())
                .build();
    }
}
