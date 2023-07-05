package com.owori.domain.family.service;

import com.owori.domain.family.dto.request.AddMemberRequest;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.entity.Invite;
import com.owori.domain.family.exception.InviteCodeDuplicateException;
import com.owori.domain.family.mapper.FamilyMapper;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService implements EntityLoader<Family, UUID> {
    private final FamilyRepository familyRepository;
    private final FamilyMapper familyMapper;
    private final AuthService authService;

    public InviteCodeResponse saveFamily(final FamilyRequest familyRequest) {
        Member member = authService.getLoginUser();
        String code = generateRandomInviteCode();
        familyRepository.save(familyMapper.toEntity(familyRequest, member, code));

        return new InviteCodeResponse(code);
    }

    private void validateCode(String code) {
        if (familyRepository.existsByInviteCode(code)) {
            throw new InviteCodeDuplicateException();
        }
    }

    private String generateRandomInviteCode() {
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        validateCode(code);
        return code;
    }

    public void addMember(final AddMemberRequest addMemberRequest) {
        familyRepository.findByInviteCode(addMemberRequest.getInviteCode().strip())
                .ifPresent(family -> {
                    Invite invite = family.getInvite();
                    if (isValidCode(invite)) {
                        invite.delete();
                        return;
                    }
                    family.addMember(authService.getLoginUser());
                });
    }

    private boolean isValidCode(Invite invite) {
        return invite.getBaseTime().getCreatedAt().plusMinutes(30L).isBefore(LocalDateTime.now());
    }

    @Override
    public Family loadEntity(final UUID id) {
        return familyRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
