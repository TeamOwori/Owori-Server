package com.owori.domain.schedule.dto.response;

import com.owori.domain.member.entity.Color;
import com.owori.domain.schedule.entity.Alarm;
import com.owori.domain.schedule.entity.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleByMonthResponse {
    private UUID id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private ScheduleType scheduleType;
    private String memberNickname;
    private Color color;
    private Boolean dDayOption;
    private List<Alarm> alarmOptions;
}
