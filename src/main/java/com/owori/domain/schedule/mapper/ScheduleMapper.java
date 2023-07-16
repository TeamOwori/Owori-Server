package com.owori.domain.schedule.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.response.FindDDayByFamilyResponse;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.entity.Schedule;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
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

    public List<FindScheduleByMonthResponse> toMonthResponseList(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::toMonthResponse)
                .toList();
    }

    public List<FindDDayByFamilyResponse> toDDayResponseList(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> toDDayResponse(schedule, toDDay(schedule.getStartDate())))
                .toList();
    }

    private FindScheduleByMonthResponse toMonthResponse(Schedule schedule) {
        return FindScheduleByMonthResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .scheduleType(schedule.getScheduleType())
                .memberNickname( schedule.getMember().getNickname())
                .color(schedule.getMember().getColor())
                .dDayOption(schedule.getDDayOption())
                .alarmOptions(schedule.getAlarmList())
                .build();
    }

    private FindDDayByFamilyResponse toDDayResponse(Schedule schedule, String dDay) {
        return FindDDayByFamilyResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .dDay(dDay)
                .scheduleType(schedule.getScheduleType())
                .memberNickname(schedule.getMember().getNickname())
                .color(schedule.getMember().getColor())
                .dDayOption(schedule.getDDayOption())
                .alarmOptions(schedule.getAlarmList())
                .build();
    }

    private String toDDay(LocalDate toDate) {
        int days = Period.between(LocalDate.now(), toDate).getDays();
        if(days == 0) return "D-DAY";
        return "D-" + days;
    }
    public LocalDate toFirstDate(String yearMonth) {
        return LocalDate.parse(yearMonth + "-01");
    }
}
