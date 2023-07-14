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

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final SayingService sayingService;
    private final AuthService authService;

    public FindHomeResponse findHomeData() {
        return null; // todo : 로직 작성
    }
}
