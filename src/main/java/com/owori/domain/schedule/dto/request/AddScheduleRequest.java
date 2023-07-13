package com.owori.domain.schedule.dto.request;

import com.owori.domain.schedule.entity.Alarm;
import com.owori.domain.schedule.entity.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddScheduleRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private ScheduleType scheduleType; // 가족 or 개인
    private Boolean dDayOption;
    private List<Alarm> alarmOptions;

}
