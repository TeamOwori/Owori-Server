package com.owori.domain.saying.service;

import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.global.audit.BaseTime;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public List<FindSayingByFamilyResponse> findSayingByFamily() {
        // BaseTime baseTime = new BaseTime();
        // Optional.ofNullable(baseTime.getUpdatedAt()).orElse(baseTime.getCreatedAt());
        return null; // todo: 로직 작성
    }

}
