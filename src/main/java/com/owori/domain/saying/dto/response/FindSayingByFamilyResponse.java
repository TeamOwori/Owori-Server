package com.owori.domain.saying.dto.response;

import com.owori.domain.saying.entity.Saying;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class FindSayingByFamilyResponse {
    private UUID id;
    private String content;
    private UUID memberId;
    private List<UUID> tagMembersId;
}
