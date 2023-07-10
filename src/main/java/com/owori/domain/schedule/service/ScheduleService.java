package com.owori.domain.schedule.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.entity.Schedule;
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
import java.util.stream.Collectors;

import static com.owori.domain.schedule.entity.ScheduleType.개인;

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
        // id값으로 찾기
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(EntityNotFoundException::new);
        // 로그인 중인 유저 받기
        Member member = authService.getLoginUser();
        // 현재 일정이 개인 일정이고 현재 사용자와 생성자가 다를 경우
        if(schedule.getScheduleType() == 개인 && schedule.getMember().getId() != member.getId()) {
            // 수정 권한이 없으므로 에러처리
            throw new NoAuthorityUpdateException();
        }

        // 가족에서 개인으로 수정할 수 있다면 생성자를 현재 수정한 유저로 변경해줘야함 -> 물어보기

        schedule.updateSchedule(updateScheduleRequest);
        return new IdResponse<>(scheduleId);
    }

    public List<FindScheduleByMonthResponse> findScheduleByMonth(String month) {
        // 넘겨받은 'yyyy'-MM' 통해 해당 달의 사작일과 마지막일 찾기
        String date = month + "-01";
        LocalDate fisrtDate = LocalDate.parse(date);
        LocalDate lastDate = fisrtDate.withDayOfMonth(fisrtDate.lengthOfMonth());

        // 일정 저장할 리스트 생성
        List<FindScheduleByMonthResponse> monthSchedule = new ArrayList<>();

        // 현재 로그인 중인 유저 받기
        Member member = authService.getLoginUser();
        // 현재 유저 가족 정보 받아오기
        Family family = member.getFamily();

        List<Schedule> schedules = scheduleRepository.findByMonth(member, fisrtDate,lastDate);
        monthSchedule = schedules.stream()
                .map(schedule -> new FindScheduleByMonthResponse(schedule.getTitle(), schedule.getStartDate(),
                        schedule.getEndDate(), schedule.getMember().getColor(), schedule.getDDayOption(), schedule.getAlarmList()))
                .collect(Collectors.toList());
        /*
        // 가족에 포함된 유저들 정보 받기
        Set<Member> familyMembers = family.getMembers();


        // 생성자를 통해 가족 일정들 받아오기
        for(Member familyMember : familyMembers) {
            // List<Schedule> 형태로 받아옴
            List<Schedule> schedules = scheduleRepository.findByMonth(familyMember, fisrtDate, lastDate);
            // List<FindScheduleMonthResponse> 형태로 변환하여 추가
            List<FindScheduleByMonthResponse> tmp = schedules.stream()
                    .map(schedule -> new FindScheduleByMonthResponse(schedule.getTitle(), schedule.getStartDate(),
                            schedule.getEndDate(), schedule.getMember().getColor(), schedule.getDDayOption(), schedule.getAlarmList()))
                    .toList();
            monthSchedule.addAll(tmp);
        }
         */

        // startDate 를 기준으로 정렬
        // 정렬 기준 선언
        Comparator<FindScheduleByMonthResponse> comparator = (prod1, prod2) -> Math.toIntExact(prod1.getStartDate().compareTo(prod2.getStartDate()));
        monthSchedule.sort(comparator);

        return monthSchedule;
    }

    @Override
    public Schedule loadEntity(UUID uuid) {
        return scheduleRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}
