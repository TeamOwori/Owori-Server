package com.owori.config.security.oauth;

import com.owori.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserPrincipleMapper {
    public UserPrinciple mapToLoginUser(Member user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", user.getId());
        return new UserPrinciple(user, user.getRole(), attributes);
    }
}
