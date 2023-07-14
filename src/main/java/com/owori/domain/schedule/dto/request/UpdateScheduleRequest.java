package com.owori.domain.schedule.dto.request;

import com.owori.domain.schedule.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean dDayOption;
    private List<Alarm> alarmOptions;
}

