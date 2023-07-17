package com.owori.domain.saying.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class SayingByFamilyResponse {
    private UUID id;
    private String content;
    private UUID memberId;
    private List<UUID> tagMembersId;
    private LocalDateTime updatedAt;
}
