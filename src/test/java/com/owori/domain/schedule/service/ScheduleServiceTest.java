package com.owori.domain.schedule.service;

import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.family.service.FamilyService;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.schedule.dto.request.AddScheduleRequest;
import com.owori.domain.schedule.dto.request.UpdateScheduleRequest;
import com.owori.domain.schedule.dto.response.ScheduleByMonthResponse;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import com.owori.domain.schedule.dto.response.ScheduleIdResponse;
import com.owori.domain.schedule.entity.Schedule;
import com.owori.domain.schedule.entity.ScheduleType;
import com.owori.domain.schedule.repository.ScheduleRepository;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.owori.domain.schedule.entity.Alarm.*;
import static org.assertj.core.api.Assertions.assertThat;



@DatabaseTest
@DisplayName("Schedule 서비스의")
public class ScheduleServiceTest extends LoginTest {

    @Autowired private ScheduleService scheduleService;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private FamilyService familyService;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private AuthService authService;

    @Test
    @DisplayName("일정 생성이 수행되는가")
    void addSchedule() {
        // given
        AddScheduleRequest request = new AddScheduleRequest("가족 여행", LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), ScheduleType.FAMILY, true, List.of(TODAY, A_DAY_AGO));

        // when
        UUID uuid = scheduleService.addSchedule(request).getScheduleId();

        // then
        Schedule schedule = scheduleRepository.findById(uuid).orElseThrow();

        assertThat(schedule.getTitle()).isEqualTo(request.getTitle());
        assertThat(schedule.getMember()).isEqualTo(authService.getLoginUser());
    }

    @Test
    @DisplayName("일정 수정이 수행되는가")
    void updateSchedule() {
        // given
        String title = "가족 여행";
        Schedule oldSchedule = scheduleRepository.save(new Schedule("가족 여", LocalDate.parse("2023-07-30"), LocalDate.parse("2023-08-02"), ScheduleType.FAMILY,false, List.of(TODAY, A_DAY_AGO), authService.getLoginUser()));
        UpdateScheduleRequest request = new UpdateScheduleRequest(oldSchedule.getId(), title, LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), true, List.of(TODAY, A_DAY_AGO));

        // when
        ScheduleIdResponse response = scheduleService.updateSchedule(request);
        Optional<Schedule> newSchedule = scheduleRepository.findById(response.getScheduleId());

        // then
        newSchedule.ifPresent(schedule -> {assertThat(schedule.getTitle()).isEqualTo(title);
                                            assertThat(schedule.getDDayOption()).isTrue();
                                            assertThat(schedule.getStartDate()).isEqualTo(LocalDate.parse("2023-07-31"));
        });
    }

    @Test
    @DisplayName("일정 삭제가 수행되는가")
    void deleteSchedule() {
        // given
        Schedule schedule = scheduleRepository.save(new Schedule("가족 여행", LocalDate.parse("2023-07-30"), LocalDate.parse("2023-08-02"), ScheduleType.FAMILY,false, List.of(TODAY, A_DAY_AGO), authService.getLoginUser()));

        // when
        scheduleService.deleteSchedule(schedule.getId());
        Optional<Schedule> deleteSchedule = scheduleRepository.findById(schedule.getId());

        // then
        assertThat(deleteSchedule).isEmpty();
    }

    @Test
    @DisplayName("월별 일정 날짜순 조회가 수행되는가")
    void findScheduleByMonth() {
        // given
        String month = "2023-07";

        // 가족 생성
        String familyName = "오월이 가족";
        String code = familyService.saveFamily(new FamilyRequest(familyName)).getInviteCode();

        // 가족 구성원 생성
        Member member1 = Member.builder().oAuth2Info(new OAuth2Info("123123", AuthProvider.APPLE)).build();
        Member member2 = Member.builder().oAuth2Info(new OAuth2Info("123126", AuthProvider.APPLE)).build();
        Member saveMember1 = memberRepository.save(member1);
        memberRepository.save(member2);

        // 가족에 멤버 추가
        Optional<Family> family = familyRepository.findByInviteCode(code);
        if(family.isPresent()) {
            family.get().addMember(saveMember1);
            family.get().addMember(authService.getLoginUser());
        }

        // 일정 생성
        Schedule schedule5 = scheduleRepository.save(new Schedule("가족여행",LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), ScheduleType.FAMILY, true, List.of(A_DAY_AGO, A_WEEK_AGO), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("코딩 테스트", LocalDate.parse("2023-06-22"), LocalDate.parse("2023-06-23"), ScheduleType.INDIVIDUAL, true, List.of(TODAY), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("친구랑 여행", LocalDate.parse("2023-07-08"), LocalDate.parse("2023-07-09"), ScheduleType.INDIVIDUAL,true, List.of(TODAY), member2));
        Schedule schedule4 = scheduleRepository.save(new Schedule("카카오 면접", LocalDate.parse("2023-07-11"), LocalDate.parse("2023-07-11"), ScheduleType.INDIVIDUAL,true, List.of(), member1));
        scheduleRepository.save(new Schedule("친구랑 여행", LocalDate.parse("2023-08-01"), LocalDate.parse("2023-08-10"), ScheduleType.INDIVIDUAL,true, List.of(A_WEEK_AGO), authService.getLoginUser()));
        Schedule schedule2 = scheduleRepository.save(new Schedule("기말고사", LocalDate.parse("2023-06-22"), LocalDate.parse("2023-07-06"), ScheduleType.INDIVIDUAL,true, List.of(A_DAY_AGO), member1));

        // when
        List<ScheduleByMonthResponse> responses = scheduleService.findScheduleByMonth(month);

        // then
        assertThat(responses.stream().map(ScheduleByMonthResponse::getScheduleId).toList()).isEqualTo(List.of(schedule2.getId(), schedule4.getId(), schedule5.getId()));
    }

    @Test
    @DisplayName("가족별 디데이 일정 조회가 수행되는가")
    void findDDayByFamily() {
        // given
        // 가족 생성
        String familyName = "오월이 가족";
        String code = familyService.saveFamily(new FamilyRequest(familyName)).getInviteCode();

        // 가족 구성원 생성
        Member member1 = Member.builder().oAuth2Info(new OAuth2Info("123123", AuthProvider.APPLE)).build();
        Member member2 = Member.builder().oAuth2Info(new OAuth2Info("123126", AuthProvider.APPLE)).build();
        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);

        // 가족에 멤버 추가
        Optional<Family> family = familyRepository.findByInviteCode(code);
        if(family.isPresent()) {
            family.get().addMember(saveMember1);
            family.get().addMember(authService.getLoginUser());
        }

        // 일정 생성
        Schedule schedule5 = scheduleRepository.save(new Schedule("가족여행",LocalDate.parse("2023-07-31"), LocalDate.parse("2023-08-02"), ScheduleType.FAMILY, true, List.of(A_DAY_AGO, A_WEEK_AGO), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("코딩 테스트", LocalDate.parse("2023-06-22"), LocalDate.parse("2023-06-23"), ScheduleType.INDIVIDUAL, true, List.of(TODAY), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("친구랑 여행", LocalDate.parse("2023-07-08"), LocalDate.parse("2023-07-09"), ScheduleType.INDIVIDUAL,true, List.of(TODAY), saveMember2));
        scheduleRepository.save(new Schedule("카카오 면접", LocalDate.parse("2023-07-20"), LocalDate.parse("2023-07-24"), ScheduleType.INDIVIDUAL,false, List.of(), saveMember1));
        Schedule schedule6 = scheduleRepository.save(new Schedule("친구랑 여행", LocalDate.parse("2023-08-01"), LocalDate.parse("2023-08-10"), ScheduleType.INDIVIDUAL,true, List.of(A_WEEK_AGO), authService.getLoginUser()));
        scheduleRepository.save(new Schedule("기말고사", LocalDate.parse("2023-06-22"), LocalDate.parse("2023-07-06"), ScheduleType.INDIVIDUAL,true, List.of(A_DAY_AGO), saveMember1));

        // when
        List<ScheduleDDayResponse> responses = scheduleService.findDDayByFamily();

        // then
        assertThat(responses.stream().map(ScheduleDDayResponse::getScheduleId).toList()).isEqualTo(List.of());
    }

    @Test
    @DisplayName("id를 통한 조회가 수행되는가")
    void loadEntity() {
        // given
        Schedule schedule = scheduleRepository.save(new Schedule("코딩 테스트", LocalDate.parse("2023-06-22"), LocalDate.parse("2023-06-23"), ScheduleType.INDIVIDUAL, true, List.of(TODAY), authService.getLoginUser()));

        // when
        Schedule result = scheduleService.loadEntity(schedule.getId());

        // then
        assertThat(schedule).isEqualTo(result);
    }
}
