package com.owori.domain.member.dto.request;

import com.owori.domain.member.entity.EmotionalBadge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionalBadgeRequest {
    private EmotionalBadge emotionalBadge;
}
