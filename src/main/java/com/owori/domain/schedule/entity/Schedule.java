package com.owori.domain.schedule.entity;

import com.owori.domain.member.entity.Member;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class) // 자동으로 시간 매핑
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule implements Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    // 개인이라면 색을 받아오기 위해서 생성자 저장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    private Boolean dDayOption;

    // 알람 옵션이 여러개일 수 있음
    @Enumerated(EnumType.STRING)
    @JoinColumn
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Alarm> alarmList = new ArrayList<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Schedule(String title, LocalDate startDate, LocalDate endDate, ScheduleType scheduleType, Boolean dDayOption, List<Alarm> alarmList, Member member){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.scheduleType = scheduleType;
        this.dDayOption = dDayOption;
        this.alarmList = alarmList;
        this.member = member;
    }

    public void updateSchedule(String title, LocalDate startDate, LocalDate endDate, Boolean dDayOption, List<Alarm> alarmList){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dDayOption = dDayOption;
        this.alarmList = alarmList;
    }
}
