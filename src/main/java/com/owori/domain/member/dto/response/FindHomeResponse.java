package com.owori.domain.member.dto.response;

import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.schedule.dto.response.FindDDayByFamilyResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindHomeResponse {
    private String familyName;
    private List<MemberProfileResponse> memberProfiles;
    private List<FindDDayByFamilyResponse> dDaySchedules;
    private List<String> familyImages;
    private List<FindSayingByFamilyResponse> sayings;

}
