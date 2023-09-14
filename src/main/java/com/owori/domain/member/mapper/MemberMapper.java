package com.owori.domain.member.mapper;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.family.entity.Family;
import com.owori.domain.member.dto.response.*;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Color;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.schedule.dto.response.FindAllScheduleByDDayResponse;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MemberMapper {
    public Member toEntity(String clientId, AuthProvider authProvider) {
        return Member.builder()
                .oAuth2Info(new OAuth2Info(clientId, authProvider))
                .build();
    }

    public MyPageProfileResponse toMyPageProfileResponse(Member member, Long storyCount, Long heartCount) {
        return MyPageProfileResponse.builder()
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .color(member.getColor())
                .emotionalBadge(member.getEmotionalBadge())
                .profileImage(member.getProfileImage())
                .storyCount(storyCount)
                .heartCount(heartCount)
                .build();
    }

    public MemberJwtResponse toJwtResponse(JwtToken jwtToken, UUID memberId, Boolean isServiceMember) {
        return MemberJwtResponse.builder()
                .jwtToken(jwtToken)
                .memberId(memberId)
                .isServiceMember(isServiceMember)
                .build();
    }

    public MemberHomeResponse toHomeResponse(Member nowMember, List<FindAllScheduleByDDayResponse> dDayByFamilyResponses, List<SayingByFamilyResponse> sayingResponses) {
        Family family = nowMember.getFamily();
        return MemberHomeResponse.builder()
                .familyGroupName(family.getFamilyGroupName())
                .memberProfiles(toProfileResponseList(nowMember, family.getMembers()))
                .ddaySchedules(dDayByFamilyResponses)
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

    public MemberColorResponse toColorResponse(Set<Member> familyMembers) {
        if (familyMembers.size() >= 7) {
            return defaultColorResponse();
        }
        return toColorResponseWithFamilyColors(familyMembers.stream().map(Member::getColor).toList());
    }

    private MemberColorResponse toColorResponseWithFamilyColors(List<Color> familyColors) {
        return MemberColorResponse.builder()
                .red(familyColors.contains(Color.RED))
                .yellow(familyColors.contains(Color.YELLOW))
                .green(familyColors.contains(Color.GREEN))
                .pink(familyColors.contains(Color.PINK))
                .skyblue(familyColors.contains(Color.SKYBLUE))
                .blue(familyColors.contains(Color.BLUE))
                .purple(familyColors.contains(Color.PURPLE))
                .build();
    }

    private MemberColorResponse defaultColorResponse() {
        return MemberColorResponse.builder().build();
    }
}
