package com.owori.domain.family.service;

import com.owori.domain.family.dto.request.AddMemberRequest;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService implements EntityLoader<Family, UUID> {
    private final FamilyRepository familyRepository;

    public InviteCodeResponse saveFamily(FamilyRequest familyRequest) {
        return null; // todo 로직 작성
    }

    public void addMember(AddMemberRequest addMemberRequest) {
        // todo 로직 작성
    }

    @Override
    public Family loadEntity(UUID id) {
        return familyRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
