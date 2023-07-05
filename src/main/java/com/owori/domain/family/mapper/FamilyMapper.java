package com.owori.domain.family.mapper;

import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class FamilyMapper {
    public Family toEntity(FamilyRequest familyRequest, Member member, String code) {
        return Family.builder()
                .familyGroupName(familyRequest.getFamilyGroupName())
                .member(member)
                .code(code)
                .build();
    }
}
