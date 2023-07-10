package com.owori.domain.schedule.dto.response;

import com.owori.domain.schedule.entity.Alarm;
import com.owori.domain.member.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindScheduleByMonthResponse {
    private UUID id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Color color;
    private Boolean dDayOption;
    private List<Alarm> alarmOptions;
}
