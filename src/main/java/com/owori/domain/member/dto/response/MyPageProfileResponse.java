package com.owori.domain.member.dto.response;

import com.owori.domain.member.entity.Color;
import com.owori.domain.member.entity.EmotionalBadge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageProfileResponse {
    private String nickname;
    private LocalDate birthday;
    private Color color;
    private EmotionalBadge emotionalBadge;
}
