package com.owori.domain.family.entity;

import com.owori.domain.member.entity.Member;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Family implements Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String familyGroupName;

    @OneToMany(mappedBy = "family", cascade = CascadeType.PERSIST)
    private Set<Member> members = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "family")
    private Invite invite;

    @JoinColumn
    @ElementCollection
    private List<String> images = new ArrayList<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Family(String familyGroupName, Member member, String code) {
        this.familyGroupName = familyGroupName;
        organizeInvite(code);
        addMember(member);
    }

    public void organizeInvite(String code) {
        this.invite = new Invite(code, this);
    }

    public void addMember(Member member) {
        this.members.add(member);
        member.organizeFamily(this);
    }

    public void updateGroupName(String familyGroupName) {
        this.familyGroupName = familyGroupName;
    }

    public void addImage(String image) {
        this.images.add(image);
    }
}
