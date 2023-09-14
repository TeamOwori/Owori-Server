package com.owori.domain.schedule.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.response.FindAllScheduleByMonthResponse;
import com.owori.domain.schedule.dto.response.FindAllScheduleByDDayResponse;
import com.owori.domain.schedule.entity.Schedule;
import com.owori.domain.schedule.entity.ScheduleType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
public class ScheduleMapper {
    public Schedule toEntity(AddScheduleRequest request, Member member) {
        return Schedule.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .scheduleType(request.getScheduleType())
                .dDayOption(request.getDdayOption())
                .alarmList(request.getAlarmOptions())
                .member(member)
                .build();
    }

    public List<FindAllScheduleByMonthResponse> toMonthResponseList(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::toMonthResponse)
                .toList();
    }

    public List<FindAllScheduleByDDayResponse> toDDayResponseList(List<Schedule> schedules, Member nowMember) {
        return schedules.stream()
                .map(schedule -> toDDayResponse(schedule, toDDay(schedule.getStartDate()), nowMember))
                .toList();
    }

    private FindAllScheduleByMonthResponse toMonthResponse(Schedule schedule) {
        return FindAllScheduleByMonthResponse.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .scheduleType(schedule.getScheduleType())
                .nickname(schedule.getMember().getNickname())
                .color(schedule.getMember().getColor())
                .ddayOption(schedule.getDDayOption())
                .alarmOptions(schedule.getAlarmList())
                .build();
    }

    private FindAllScheduleByDDayResponse toDDayResponse(Schedule schedule, String dDay, Member nowMember) {
        return FindAllScheduleByDDayResponse.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .dday(dDay)
                .scheduleType(schedule.getScheduleType())
                .nickname(schedule.getMember().getNickname())
                .color(schedule.getMember().getColor())
                .ddayOption(schedule.getDDayOption())
                .alarmOptions(schedule.getAlarmList())
                .isMine(checkMine(schedule, nowMember))
                .build();
    }

    private Boolean checkMine(Schedule schedule, Member nowMember) {
        if(schedule.getMember().getId() != nowMember.getId() && schedule.getScheduleType() == ScheduleType.INDIVIDUAL) return Boolean.FALSE;
        return Boolean.TRUE;
    }

    private String toDDay(LocalDate toDate) {
        int days = Period.between(LocalDate.now(), toDate).getDays();
        if (days == 0) return "D-DAY";
        return "D-" + days;
    }

    public LocalDate toFirstDate(String yearMonth) {
        return LocalDate.parse(yearMonth + "-01");
    }
}
