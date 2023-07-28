package com.owori.domain.saying.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SayingIdResponse {
    private UUID sayingId;
}
