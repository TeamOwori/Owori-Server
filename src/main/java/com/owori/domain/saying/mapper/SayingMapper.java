package com.owori.domain.saying.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.entity.SayingTagMember;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SayingMapper {
    public Saying toEntity(String content, Member member, List<Member> tagMembers) {
        return Saying.builder()
                .content(content)
                .member(member)
                .tagMembers(tagMembers)
                .build();
    }

    public List<FindSayingByFamilyResponse> toResponseList(List<Saying> sayingList) {
        return sayingList.stream()
                .map(this::toResponse)
                .toList();
    }

    public FindSayingByFamilyResponse toResponse(Saying saying) {
        return FindSayingByFamilyResponse.builder()
                .id(saying.getId())
                .content(saying.getContent())
                .memberId(saying.getMember().getId())
                .tagMembersId(getTagMembersId(saying))
                .updatedAt(getUpdatedAt(saying))
                .build();
    }

    private LocalDateTime getUpdatedAt(Saying saying) {
        return Optional.ofNullable(saying.getBaseTime().getUpdatedAt()).orElse(saying.getBaseTime().getCreatedAt());
    }

    private List<UUID> getTagMembersId(Saying saying) {
        return saying.getTagMembers().stream().map(SayingTagMember::getMember).map(Member::getId).toList();
    }
}
