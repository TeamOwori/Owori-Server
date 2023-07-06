package com.owori.domain.Schedule.dto.request;

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
    private String type; // 가족 or 개인
    private Boolean dDayOption;
    private List<String> alarmOptions;
}

