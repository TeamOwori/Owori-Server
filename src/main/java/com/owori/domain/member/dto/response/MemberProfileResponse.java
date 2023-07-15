package com.owori.domain.member.dto.response;

import com.owori.domain.member.entity.EmotionalBadge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileResponse {
    private UUID id;
    private String nickName;
    private String profileImage;
    private EmotionalBadge emotionalBadge;
}
