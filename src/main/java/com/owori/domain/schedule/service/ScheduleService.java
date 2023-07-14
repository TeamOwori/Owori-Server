package com.owori.domain.schedule.service;

import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.entity.Schedule;
import com.owori.domain.schedule.entity.ScheduleType;
import com.owori.domain.schedule.exception.NoAuthorityUpdateException;
import com.owori.domain.schedule.mapper.ScheduleMapper;
import com.owori.domain.schedule.repository.ScheduleRepository;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService implements EntityLoader<Schedule, UUID> {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final AuthService authService;

    public IdResponse<UUID> addSchedule(AddScheduleRequest addScheduleRequest) {
        Member member = authService.getLoginUser();
        Schedule newSchedule = scheduleRepository.save(scheduleMapper.toEntity(addScheduleRequest, member));
        return new IdResponse<>(newSchedule.getId());
    }

    @Transactional
    public IdResponse<UUID> updateSchedule(UUID scheduleId, UpdateScheduleRequest updateScheduleRequest) {
        // id 값으로 일정 찾기
        Schedule schedule = loadEntity(scheduleId);
        // 로그인 중인 유저 받기
        Member member = authService.getLoginUser();
        // 현재 일정이 개인 일정이고 현재 사용자와 생성자가 다를 경우 예외처리
        if(schedule.getScheduleType().equals(ScheduleType.INDIVIDUAL) && !member.equals(schedule.getMember())) throw new NoAuthorityUpdateException();

        schedule.updateSchedule(updateScheduleRequest.getTitle(), updateScheduleRequest.getStartDate(),
                updateScheduleRequest.getEndDate(), updateScheduleRequest.getDDayOption(), updateScheduleRequest.getAlarmOptions());

        return new IdResponse<>(scheduleId);
    }

    public List<FindScheduleByMonthResponse> findScheduleByMonth(String month) {
        // 넘겨받은 'yyyy'-MM' 통해 해당 달의 사작일과 마지막일 찾기
        String date = month + "-01";
        LocalDate fisrtDate = LocalDate.parse(date);
        LocalDate lastDate = fisrtDate.withDayOfMonth(fisrtDate.lengthOfMonth());

        // 현재 로그인 중인 유저 받기
        Member member = authService.getLoginUser();
        // 현재 유저 가족에 포함된 회원 정보 받기
        Set<Member> familyMembers = member.getFamily().getMembers();

        // 가족들의 일정 시작일 기준으로 정렬해서 받기
        List<Schedule> monthSchedule = new ArrayList<>(familyMembers.stream()
                .map(familyMember -> scheduleRepository.findByMonth(familyMember, fisrtDate, lastDate))
                .flatMap(List::stream)
                .toList());
        monthSchedule.sort(Comparator.comparing(Schedule::getStartDate));

        return scheduleMapper.toResponseList(monthSchedule);
    }

    @Override
    public Schedule loadEntity(UUID uuid) {
        return scheduleRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}
