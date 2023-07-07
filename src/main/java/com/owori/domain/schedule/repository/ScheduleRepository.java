package com.owori.domain.schedule.repository;

import com.owori.domain.schedule.entity.Schedule;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Schedule> findById(UUID id);
}
