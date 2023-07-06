package com.owori.domain.member.entity;

import com.owori.domain.family.entity.Family;
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

    private String nickname;

    private String profileImage;

    private LocalDate birthDay;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Color color;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Family family;

    @JoinColumn
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> role = new ArrayList<>(List.of(Role.ROLE_USER));

    @Embedded
    private OAuth2Info oAuth2Info;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Member(OAuth2Info oAuth2Info) {
        this.oAuth2Info = oAuth2Info;
    }

    public void update(String nickname, LocalDate birthDay) {
        this.nickname = nickname;
        this.birthDay = birthDay;
        this.color = generateColor();
    }

    private Color generateColor() {
        List<Color> familyColors = family.getMembers().stream().filter(m -> !m.getId().equals(this.id)).map(Member::getColor).toList();
        return Color.getNextColor(familyColors);
    }

    public List<SimpleGrantedAuthority> getRole() {
        return role.stream().map(Role::name).map(SimpleGrantedAuthority::new).toList();
    }

    public void organizeFamily(Family family) {
        this.family = family;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
