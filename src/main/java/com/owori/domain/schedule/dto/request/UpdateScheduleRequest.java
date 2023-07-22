package com.owori.domain.schedule.dto.request;

import com.owori.domain.schedule.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleRequest {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotNull(message = "startDate는 필수 입력값입니다.")
    @FutureOrPresent(message = "startDate에 과거 날짜를 입력할 수 없습니다.")
    private LocalDate startDate;
    @NotNull(message = "endDate는 필수 입력값입니다.")
    private LocalDate endDate;
    private Boolean ddayOption;
    @Size(max = 3, message = "알람 옵션은 최대 세 개입니다.")
    private List<Alarm> alarmOptions;
}

