package com.owori.domain.schedule.service;

import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.ScheduleByMonthResponse;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import com.owori.domain.schedule.dto.response.ScheduleIdResponse;
import com.owori.domain.schedule.entity.Schedule;
import com.owori.domain.schedule.entity.ScheduleType;
import com.owori.domain.schedule.mapper.ScheduleMapper;
import com.owori.domain.schedule.repository.ScheduleRepository;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.exception.NoAuthorityException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService implements EntityLoader<Schedule, UUID> {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final AuthService authService;

    public ScheduleIdResponse addSchedule(AddScheduleRequest addScheduleRequest) {
        Member member = authService.getLoginUser();
        Schedule newSchedule = scheduleRepository.save(scheduleMapper.toEntity(addScheduleRequest, member));
        return new ScheduleIdResponse(newSchedule.getId());
    }

    @Transactional
    public ScheduleIdResponse updateSchedule(UpdateScheduleRequest updateScheduleRequest) {
        // id 값으로 일정 찾기
        UUID scheduleId = updateScheduleRequest.getScheduleId();
        Schedule schedule = loadEntity(scheduleId);
        // 현재 일정이 개인 일정이고 현재 사용자와 생성자가 다를 경우 예외처리
        if (nonValidMember(schedule)) throw new NoAuthorityException();

        schedule.updateSchedule(updateScheduleRequest.getTitle(), updateScheduleRequest.getStartDate(),
                updateScheduleRequest.getEndDate(), updateScheduleRequest.getDdayOption(), updateScheduleRequest.getAlarmOptions());

        return new ScheduleIdResponse(scheduleId);
    }

    private boolean nonValidMember(Schedule schedule) {
        return schedule.getScheduleType().equals(ScheduleType.INDIVIDUAL) && !authService.getLoginUser().getId().equals(schedule.getMember().getId());
    }

    @Transactional
    public void deleteSchedule(UUID scheduleId) {
        Schedule schedule = loadEntity(scheduleId);
        // 생성자와 동일하지 않을 경우 예외처리
        if (!authService.getLoginUser().getId().equals(schedule.getMember().getId())) throw new NoAuthorityException();
        schedule.delete();
    }

    @Transactional(readOnly = true)
    public List<ScheduleByMonthResponse> findScheduleByMonth(String month) {
        // 넘겨받은 'yyyy'-MM' 통해 해당 달의 사작일과 마지막일 찾기
        LocalDate firstDate = scheduleMapper.toFirstDate(month);
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        // 현재 유저 가족에 포함된 회원 정보 받기
        Set<Member> familyMembers = authService.getLoginUser().getFamily().getMembers();

        // 가족들의 일정 시작일 기준으로 정렬해서 받기
        List<Schedule> monthSchedules = familyMembers.stream()
                .map(familyMember -> scheduleRepository.findAllByMonth(familyMember, firstDate, lastDate))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Schedule::getStartDate))
                .toList();

        return scheduleMapper.toMonthResponseList(monthSchedules);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDDayResponse> findDDayByFamily() {

        // 현재 유저 가족에 포함된 회원 정보 받기
        Set<Member> familyMembers = authService.getLoginUser().getFamily().getMembers();

        // 가족들의 일정 중 dDay 옵션이 켜진 일정을 시작일 기준으로 정렬해서 받기
        List<Schedule> dDaySchedules = familyMembers.stream()
                .map(familyMember -> scheduleRepository.findAllByMember(familyMember, Boolean.TRUE, LocalDate.now()))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Schedule::getStartDate))
                .toList();

        return scheduleMapper.toDDayResponseList(dDaySchedules);
    }

    @Override
    public Schedule loadEntity(UUID uuid) {
        return scheduleRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}
