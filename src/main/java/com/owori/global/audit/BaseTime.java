package com.owori.global.audit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseTime {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
