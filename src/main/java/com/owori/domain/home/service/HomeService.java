package com.owori.domain.home.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.family.service.FamilyService;
import com.owori.domain.home.dto.response.FindHomeResponse;
import com.owori.domain.home.dto.response.MemberProfileResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.member.service.MemberService;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.service.SayingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final SayingService sayingService;
    private final AuthService authService;

    public FindHomeResponse findHomeData() {
        // 현재 로그인 유저 정보 받아오기
        Member nowMember = authService.getLoginUser();
        Family family = nowMember.getFamily();

        // 각 유저의 프로필 정보 받기
        List<MemberProfileResponse> membersProfileData = family.getMembers().stream()
                .map(member -> new MemberProfileResponse(member.getId(), member.getNickname(), member.getProfileImage(), member.getEmotionalBadge()))
                .toList();
        // 프로필 정렬(본인 + 가족 닉네임순)

        List<FindSayingByFamilyResponse> sayingsData = sayingService.findSayingByFamily();

        return new FindHomeResponse(family.getFamilyGroupName(), membersProfileData, family.getImages(), sayingsData);
    }
}
