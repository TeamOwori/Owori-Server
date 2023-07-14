package com.owori.domain.home.controller;

import com.owori.domain.home.dto.response.FindHomeResponse;
import com.owori.domain.home.dto.response.MemberProfileResponse;
import com.owori.domain.home.service.HomeService;
import com.owori.domain.member.entity.EmotionalBadge;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.support.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.owori.support.docs.ApiDocsUtils.getDocumentRequest;
import static com.owori.support.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Home 컨트롤러의")
@WebMvcTest(HomeController.class)
public class HomeControllerTest extends RestDocsTest {
    @MockBean private HomeService homeService;

    @Test
    @DisplayName("GET / home 홈화면 조회 API 테스트")
    void findHomeDate() throws Exception {
        // given
        UUID member1 = UUID.randomUUID();
        UUID member2 = UUID.randomUUID();
        List<MemberProfileResponse> membersProfile = List.of(
                new MemberProfileResponse(member1, "아빠", "111111", EmotionalBadge.SO_HAPPY),
                new MemberProfileResponse(member2, "엄마", "222222", EmotionalBadge.JOY),
                new MemberProfileResponse(UUID.randomUUID(), "아들","333333",EmotionalBadge.CRY)
        );

        List<FindSayingByFamilyResponse> sayings = List.of(
                new FindSayingByFamilyResponse(UUID.randomUUID(), "오늘 회식해요", member1, List.of(UUID.randomUUID(), UUID.randomUUID()), LocalDateTime.now()),
                new FindSayingByFamilyResponse(UUID.randomUUID(),"오늘 저녁 카레", member2, List.of(), LocalDateTime.now())
        );

        FindHomeResponse expected = new FindHomeResponse("오월이 가족", membersProfile, List.of("111111","222222"), sayings);

        given(homeService.findHomeData()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/home")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ghuriewhv32j12.oiuwhftg32shdi.ogiurhw0gb")
                                .header("memberId", UUID.randomUUID().toString())
                );

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.family_name").exists());

        // docs
        perform.andDo(print())
                .andDo(document("find home data", getDocumentRequest(), getDocumentResponse()));
    }
}
