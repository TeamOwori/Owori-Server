package com.owori.domain.member.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Embeddable
@AllArgsConstructor
@EqualsAndHashCode(of = "clientId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Info {
    @Lob
    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
}
