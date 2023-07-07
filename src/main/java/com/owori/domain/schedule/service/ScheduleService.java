package com.owori.domain.schedule.service;

import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.domain.schedule.entity.Schedule;
import com.owori.domain.schedule.mapper.ScheduleMapper;
import com.owori.domain.schedule.repository.ScheduleRepository;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public IdResponse<UUID> updateSchedule(UUID scheduleId, UpdateScheduleRequest updateScheduleRequest) {
        return null; // todo 로직작성
    }

    public List<FindScheduleByMonthResponse> findScheduleByMonth(Pageable pageable, String month) {
        return null; // todo 로직작성
    }

    @Override
    public Schedule loadEntity(UUID uuid) {
        return scheduleRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}
