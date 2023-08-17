package com.owori.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailsRequest {
    @Size(min = 1, max = 7)
    private String nickname;
    private String birthday;
}
