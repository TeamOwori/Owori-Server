package com.owori.domain.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ScheduleIdResponse {
    private UUID scheduleId;
}
