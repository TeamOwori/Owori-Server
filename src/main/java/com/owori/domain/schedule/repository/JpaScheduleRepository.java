package com.owori.domain.schedule.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepository {

    @Query("select s from Schedule s where s.member = :member and ((s.startDate >= :firstDate and s.startDate <= :lastDate) or (s.endDate >= :firstDate and s.endDate <= :lastDate))")
    List<Schedule> findByMonth(Member member, LocalDate firstDate, LocalDate lastDate);
}
