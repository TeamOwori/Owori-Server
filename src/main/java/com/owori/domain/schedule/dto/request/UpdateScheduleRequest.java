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
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleRequest {
    @NotNull
    private UUID scheduleId;
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    private String content;
    @NotNull(message = "시작일은 필수 입력값입니다.")
    private LocalDate startDate;
    @NotNull(message = "종료일은 필수 입력값입니다.")
    @FutureOrPresent(message = "종료일에는 과거 날짜를 입력할 수 없습니다.")
    private LocalDate endDate;
    @NotNull(message = "디데이 옵션은 필수 입력값입니다")
    private Boolean ddayOption;
    @Size(max = 3, message = "알람 옵션은 최대 세 개입니다.")
    private List<Alarm> alarmOptions;
}

