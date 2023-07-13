package com.owori.domain.saying.entity;

import com.owori.domain.member.entity.Member;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

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
    @Column(nullable = false)
    @Length(max = 50)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    // 태그 기능
    @OneToMany(mappedBy = "saying", cascade = CascadeType.ALL)
    private List<SayingTagMember> tagMembers = new ArrayList<>();

    @Column(nullable = false)
    private Boolean status = Boolean.TRUE;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Saying(String content, Member member, List<Member> tagMembers) {
        this.content = content;
        this.member = member;
        organize(tagMembers);
    }

    public void update(String content, List<Member> tagMembers) {
        this.content = content;
        change(tagMembers);
    }

    private void organize(List<Member> tagMembers) {
        this.tagMembers = tagMembers.stream()
                .map(tagMember -> new SayingTagMember(this, tagMember))
                .toList();
    }


    private void change(List<Member> tagMembers) {
        // 기존에 있던거 삭제
        this.tagMembers.forEach(SayingTagMember::delete);
        // 새로운 태그된 멤버들 추가
        this.tagMembers = tagMembers.stream()
                .map(tagMember -> new SayingTagMember(this, tagMember))
                .toList();
    }

    public void changeStatus() {
        this.status = Boolean.FALSE;
        this.delete();
    }
}
