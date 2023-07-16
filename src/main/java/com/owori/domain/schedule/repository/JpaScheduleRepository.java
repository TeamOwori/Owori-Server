package com.owori.domain.schedule.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepository {

    @Query("select s from Schedule s where s.member = :member and ((s.startDate between :startDate and :endDate) or (s.endDate between :startDate and :endDate))")
    List<Schedule> findAllByMonth(Member member, LocalDate startDate, LocalDate endDate);

    @Query("select s from Schedule s where s.member = :member and s.dDayOption = :dDayOption and s.startDate >= :nowDate")
    List<Schedule> findAllByMember(Member member, Boolean dDayOption, LocalDate nowDate);
}
