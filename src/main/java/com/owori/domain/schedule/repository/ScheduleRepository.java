package com.owori.domain.schedule.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Schedule> findById(UUID id);

    // 멤버와 웗 시작일과 종료일 받아서 일정 넘겨주기
    @Query("select s from Schedule s where s.member = :member and ((s.startDate >= :firstDate and s.startDate <= :lastDate) or (s.endDate >= :firstDate and s.endDate <= :lastDate))")
    List<Schedule> findByMonth(Member member, LocalDate firstDate, LocalDate lastDate);
}
