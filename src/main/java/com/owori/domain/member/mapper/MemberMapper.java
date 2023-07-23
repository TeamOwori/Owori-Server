package com.owori.domain.member.mapper;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.family.entity.Family;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.response.MemberHomeResponse;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.dto.response.MemberProfileResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MemberMapper {
    public Member toEntity(String clientId, MemberRequest memberRequest) {
        return Member.builder()
                .oAuth2Info(new OAuth2Info(clientId, memberRequest.getAuthProvider()))
                .build();
    }

    public MemberJwtResponse toJwtResponse(JwtToken jwtToken, UUID memberId) {
        return MemberJwtResponse.builder()
                .jwtToken(jwtToken)
                .memberId(memberId)
                .build();
    }

    public MemberHomeResponse toHomeResponse(Member nowMember, List<ScheduleDDayResponse> dDayByFamilyResponses, List<SayingByFamilyResponse> sayingResponses) {
        Family family = nowMember.getFamily();
        return MemberHomeResponse.builder()
                .familyGroupName(family.getFamilyGroupName())
                .memberProfiles(toProfileResponseList(nowMember, family.getMembers()))
                .dDaySchedules(dDayByFamilyResponses)
                .familyImages(family.getImages())
                .familySayings(sayingResponses)
                .build();
    }

    private List<MemberProfileResponse> toProfileResponseList(Member nowMember, Set<Member> members) {
        members.removeIf(member -> member.equals(nowMember));
        // 닉네임순으로 정렬
        List<Member> familyMembers = members.stream().sorted(Comparator.comparing(Member::getNickname)).toList();
        MemberProfileResponse memberProfileResponse = toProfileResponse(nowMember);
        List<MemberProfileResponse> memberProfileResponseList = familyMembers.stream().map(this::toProfileResponse).collect(Collectors.toList());
        // 본인이 맨 앞으로 정렬
        memberProfileResponseList.add(0, memberProfileResponse);
        return memberProfileResponseList;
    }

    private MemberProfileResponse toProfileResponse(Member member) {
        return MemberProfileResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .emotionalBadge(member.getEmotionalBadge())
                .build();
    }

}
