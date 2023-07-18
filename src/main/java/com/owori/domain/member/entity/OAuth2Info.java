package com.owori.domain.member.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Info {
    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuth2Info that = (OAuth2Info) o;
        return Objects.equals(clientId, that.clientId) && authProvider == that.authProvider;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, authProvider);
    }
}
