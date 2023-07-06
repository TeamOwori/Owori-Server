package com.owori.domain.record.dto.response;

import com.owori.domain.member.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindRecordByMonthResponse {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Color color;
    private Boolean dDayOption;
    private List<String> alarmOptions;
}
