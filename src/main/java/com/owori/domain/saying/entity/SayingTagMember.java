package com.owori.domain.saying.entity;

import com.owori.domain.member.entity.Member;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SayingTagMember implements Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    // Saying
    @ManyToOne
    @JoinColumn(name = "SAYING_ID")
    private Saying saying;

    // Tag된 Member
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;
}
