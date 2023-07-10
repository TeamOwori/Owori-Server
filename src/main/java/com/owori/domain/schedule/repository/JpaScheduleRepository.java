package com.owori.domain.schedule.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepository {


}
