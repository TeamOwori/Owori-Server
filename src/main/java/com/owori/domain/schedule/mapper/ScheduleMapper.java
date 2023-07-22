package com.owori.domain.schedule.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import com.owori.domain.schedule.dto.response.ScheduleByMonthResponse;
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
                .dDayOption(request.getDdayOption())
                .alarmList(request.getAlarmOptions())
                .member(member)
                .build();
    }

    public List<ScheduleByMonthResponse> toMonthResponseList(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::toMonthResponse)
                .toList();
    }

    public List<ScheduleDDayResponse> toDDayResponseList(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> toDDayResponse(schedule, toDDay(schedule.getStartDate())))
                .toList();
    }

    private ScheduleByMonthResponse toMonthResponse(Schedule schedule) {
        return ScheduleByMonthResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .scheduleType(schedule.getScheduleType())
                .memberNickname( schedule.getMember().getNickname())
                .color(schedule.getMember().getColor())
                .ddayOption(schedule.getDDayOption())
                .alarmOptions(schedule.getAlarmList())
                .build();
    }

    private ScheduleDDayResponse toDDayResponse(Schedule schedule, String dDay) {
        return ScheduleDDayResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .dday(dDay)
                .scheduleType(schedule.getScheduleType())
                .memberNickname(schedule.getMember().getNickname())
                .color(schedule.getMember().getColor())
                .ddayOption(schedule.getDDayOption())
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
