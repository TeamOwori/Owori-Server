package com.owori.domain.saying.entity;

import com.owori.domain.member.entity.Member;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Saying implements Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    // 내용 글자 수 최대 50
    @Column(nullable = false, length = 50)
    private String content;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    // 태그 기능
    @OneToMany
    @JoinColumn(name = "SAYING_ID")
    private List<Member> tagMembers = new ArrayList<>();

    @Column(nullable = false)
    private Boolean status;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;
}
