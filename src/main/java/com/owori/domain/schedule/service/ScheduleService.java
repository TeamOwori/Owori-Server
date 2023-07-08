package com.owori.domain.schedule.service;

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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        // 로그인 중인 member 받기
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

    public List<FindScheduleByMonthResponse> findScheduleByMonth(Pageable pageable, String month) {
        return null; // todo 로직작성
    }

    @Override
    public Schedule loadEntity(UUID uuid) {
        return scheduleRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}
