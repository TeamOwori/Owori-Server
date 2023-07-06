package com.owori.domain.Schedule.repository;

import com.owori.domain.Schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepository {}
