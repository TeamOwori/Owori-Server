package com.owori.domain.member.dto.response;

import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberHomeResponse {
    private String familyGroupName;
    private List<MemberProfileResponse> memberProfiles;
    private List<ScheduleDDayResponse> ddaySchedules;
    private List<String> familyImages;
    private List<SayingByFamilyResponse> familySayings;
}
