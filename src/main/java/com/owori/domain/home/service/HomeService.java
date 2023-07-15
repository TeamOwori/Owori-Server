package com.owori.domain.home.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.home.dto.response.FindHomeResponse;
import com.owori.domain.home.dto.response.MemberProfileResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.service.SayingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final SayingService sayingService;
    private final AuthService authService;

    public FindHomeResponse findHomeData() {
        // 현재 로그인 유저 정보 받아오기
        Member nowMember = authService.getLoginUser();
        Family family = nowMember.getFamily();

        // 현재 유저를 제외한 가족 정보 받기
        Set<Member> familyAllMembers = family.getMembers();
        familyAllMembers.removeIf(member -> member.equals(nowMember));

        List<Member> familyMembers = familyAllMembers.stream()
                .sorted(Comparator.comparing(Member::getNickname))
                .toList();

        List<FindSayingByFamilyResponse> sayingsData = sayingService.findSayingByFamily();

        // 각 유저의 프로필 정보 받기
        List<MemberProfileResponse> membersProfileData = new java.util.ArrayList<>(List.of(new MemberProfileResponse(nowMember.getId(), nowMember.getNickname(), nowMember.getProfileImage(), nowMember.getEmotionalBadge())));
        membersProfileData.addAll(familyAllMembers.stream().map(member -> new MemberProfileResponse(member.getId(), member.getNickname(), member.getProfileImage(), member.getEmotionalBadge())).toList());


        return new FindHomeResponse(family.getFamilyGroupName(), membersProfileData, family.getImages(), sayingsData);
    }
}
