package com.owori.domain.keyword.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FindKeywordsResponse {
    private UUID keywordId;
    private String contents;
}
