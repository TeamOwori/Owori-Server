package com.owori.domain.saying.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSayingRequest {
    private String content;
    private List<UUID> tagMembersId;
}
