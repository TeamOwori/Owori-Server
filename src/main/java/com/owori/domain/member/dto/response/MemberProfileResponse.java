package com.owori.domain.member.dto.response;

import com.owori.domain.member.entity.EmotionalBadge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileResponse {
    private UUID id;
    private String nickname;
    private String profileImage;
    private EmotionalBadge emotionalBadge;
}
