package com.owori.domain.saying.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.saying.entity.Saying;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SayingMapper {
    public Saying toEntity(String content, Member member, List<Member> tagMembers) {
        return Saying.builder()
                .content(content)
                .member(member)
                .tagMembers(tagMembers)
                .build();
    }
}
