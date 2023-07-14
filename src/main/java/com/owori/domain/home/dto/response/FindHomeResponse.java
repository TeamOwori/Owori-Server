package com.owori.domain.home.dto.response;

import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
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
    // dday 정보
    private List<String> familyImages;
    private List<FindSayingByFamilyResponse> sayings;

}
