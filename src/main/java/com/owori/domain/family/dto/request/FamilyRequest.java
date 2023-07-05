package com.owori.domain.family.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyRequest {
    @Size(min = 1, max = 10)
    private String familyGroupName;
}
