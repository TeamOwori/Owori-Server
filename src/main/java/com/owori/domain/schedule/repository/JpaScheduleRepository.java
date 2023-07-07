package com.owori.domain.schedule.repository;

import com.owori.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepository {}
