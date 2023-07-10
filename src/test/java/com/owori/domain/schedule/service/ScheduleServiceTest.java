package com.owori.domain.schedule.service;

import com.owori.domain.member.entity.Color;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.entity.Schedule;
import com.owori.domain.schedule.repository.ScheduleRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static com.owori.domain.schedule.entity.Alarm.*;
import static com.owori.domain.schedule.entity.ScheduleType.가족;
import static com.owori.domain.schedule.entity.ScheduleType.개인;


@DatabaseTest
@DisplayName("Schedule 서비스의")
public class ScheduleServiceTest extends LoginTest {

    @Autowired private ScheduleService scheduleService;
    @Autowired private ScheduleRepository scheduleRepository;

    @Autowired private AuthService authService;

    @Test
    @DisplayName("일정 생성이 수행되는가")
    void addSchedule() {
        // given
        AddScheduleRequest request = new AddScheduleRequest("가족 여행", LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), 가족, true, List.of(당일, 하루전));

        // when
        UUID uuid = scheduleService.addSchedule(request).getId();

        // then
        Schedule schedule = scheduleRepository.findById(uuid).orElseThrow();

        Assertions.assertEquals(schedule.getTitle(), request.getTitle());
        Assertions.assertEquals(schedule.getMember(), authService.getLoginUser());
    }

    @Test
    @DisplayName("일정 수정이 수행되는가")
    void updateSchedule() {
        // given

    }

    @Test
    @DisplayName("월별 일정 날짜순 조회가 수행되는가")
    void findScehduleByMonth() {
        // given
        String month = "2023-07";
        scheduleRepository.save(new Schedule("코딩 테스트", LocalDate.parse("2023-06-22"), LocalDate.parse("2023-06-23"), 개인, true, List.of(당일), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("기말고사", LocalDate.parse("2023-06-22"), LocalDate.parse("2023-07-06"), 개인,true, List.of(당일), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("친구랑 여행", LocalDate.parse("2023-07-08"), LocalDate.parse("2023-07-09"), 가족,true, List.of(당일), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("가족여행",LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), 개인, true, List.of(하루전, 일주일전), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("친구랑 여행", LocalDate.parse("2023-08-01"), LocalDate.parse("2023-08-10"), 개인,true, List.of(당일), authService.getLoginUser()));

        // when
        List<FindScheduleByMonthResponse> responses = scheduleService.findScheduleByMonth(month);

        // then
        for(FindScheduleByMonthResponse response : responses) {
            Assertions.assertEquals(response.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), month);
            Assertions.assertEquals(response.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), month);
        }
    }
}
