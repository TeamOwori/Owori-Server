package com.owori.domain.family.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberRequest {
    @Size(min = 10, max = 10)
    private String inviteCode;
}
