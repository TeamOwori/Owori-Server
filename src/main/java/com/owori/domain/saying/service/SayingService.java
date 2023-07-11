package com.owori.domain.saying.service;

import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SayingService {
    private final SayingRepository sayingRepository;

    public IdResponse<UUID> addSaying(AddSayingRequest request) {
        return null; // todo: 로직 작성
    }

    @Transactional
    public IdResponse<UUID> updateSaying(UUID sayingId, UpdateSayingRequest request) {
        return null; // todo: 로직 작성
    }

    @Transactional
    public IdResponse<UUID> deleteSaying(UUID sayingId) {
        return null; // todo: 로직 작성
    }
}
