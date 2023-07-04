package com.owori.domain.member.entity;

import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    private String nickname;

    private String profileImage;

    private LocalDate birthDay;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> role = new ArrayList<>(List.of(Role.ROLE_USER));

    @Embedded
    private OAuth2Info oAuth2Info;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Member(String name, OAuth2Info oAuth2Info) {
        this.name = name;
        this.oAuth2Info = oAuth2Info;
    }

    public List<SimpleGrantedAuthority> getRole() {
        return role.stream().map(Role::name).map(SimpleGrantedAuthority::new).toList();
    }
}
