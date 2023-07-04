package com.owori.config.security.oauth;

import com.owori.config.security.oauth.attributemapper.AttributeMapper;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final UserPrincipleMapper userPrincipleMapper;
    private final AttributeMapper attributeMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        AuthProvider authProvider = AuthProvider.valueOf(
                userRequest.getClientRegistration().getClientName().toUpperCase());

        OAuth2Request oAuth2Request = attributeMapper.mapToRequest(authProvider, oAuth2User.getAttributes());

        Member user = memberService.saveIfNone(oAuth2Request);

        return userPrincipleMapper.mapToLoginUser(user);
    }
}
