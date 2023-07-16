package com.owori.domain.schedule.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Schedule> findById(UUID id);

    // 멤버와 웗 시작일과 종료일 받아서 일정 넘겨주기
    List<Schedule> findAllByMonth(Member member, LocalDate startDate, LocalDate endDate);

    // 멤버와 디데이옵션을 통해서 오늘 이후 일정 넘겨주기
    List<Schedule> findAllByMember(Member member, Boolean dDayOption, LocalDate nowDate);

}
