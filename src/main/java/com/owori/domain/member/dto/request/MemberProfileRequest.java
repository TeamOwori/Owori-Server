package com.owori.domain.member.dto.request;

import com.owori.domain.member.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileRequest {
    @Size(min = 1, max = 7)
    private String nickname;
    @PastOrPresent
    private LocalDate birthday;
    @NotNull
    private Color color;
}
