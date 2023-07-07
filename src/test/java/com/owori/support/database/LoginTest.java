package com.owori.support.database;

import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.domain.member.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@DatabaseTest
public abstract class LoginTest {

    @MockBean
    protected AuthService authService;

    @Autowired
    protected MemberRepository memberRepository;
    protected Member loginUser;

    @BeforeEach
    public void setup() {
        Member member = new Member(new OAuth2Info("123", AuthProvider.KAKAO));
        loginUser = memberRepository.save(member);
        when(authService.getLoginUserId()).thenReturn(loginUser.getId());
        when(authService.getLoginUser()).thenReturn(loginUser);
    }
}
