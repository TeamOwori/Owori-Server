package com.owori.domain.family.service;

import com.owori.domain.family.dto.request.AddMemberRequest;
import com.owori.domain.family.dto.request.DeleteFamilyimageRequest;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.FamilyImageResponse;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.entity.Invite;
import com.owori.domain.family.exception.InviteCodeDuplicateException;
import com.owori.domain.family.exception.InviteCodeExistException;
import com.owori.domain.family.mapper.FamilyMapper;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.image.service.ImageService;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.global.dto.ImageResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import com.owori.utils.S3ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService implements EntityLoader<Family, UUID> {
    private final FamilyRepository familyRepository;
    private final FamilyMapper familyMapper;
    private final AuthService authService;
    private final S3ImageComponent s3ImageComponent;
    public InviteCodeResponse saveFamily(final FamilyRequest familyRequest) {
        Member member = authService.getLoginUser();
        String code = generateRandomInviteCode();
        familyRepository.save(familyMapper.toEntity(familyRequest, member, code));

        return new InviteCodeResponse(code);
    }

    private void validateCode(final String code) {
        if (familyRepository.existsByInviteCode(code)) {
            throw new InviteCodeDuplicateException();
        }
    }

    private String generateRandomInviteCode() {
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        validateCode(code);
        return code;
    }

    @Transactional
    public void addMember(final AddMemberRequest addMemberRequest) {
        familyRepository.findByInviteCode(addMemberRequest.getInviteCode())
                .ifPresent(family -> {
                    Invite invite = family.getInvite();
                    if (!isValidCode(invite)) {
                        invite.delete();
                        return;
                    }
                    family.addMember(authService.getLoginUser());
                });
    }

    private boolean isValidCode(final Invite invite) {
        return invite.getBaseTime().getCreatedAt().plusMinutes(30L).isAfter(LocalDateTime.now());
    }

    @Override
    public Family loadEntity(final UUID id) {
        return familyRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void updateGroupName(final FamilyRequest groupNameRequest) {
        Family family = authService.getLoginUser().getFamily();
        family.updateGroupName(groupNameRequest.getFamilyGroupName());
    }

    @Transactional
    public FamilyImageResponse saveFamilyImage(final MultipartFile multipartFile) {
        Family family = authService.getLoginUser().getFamily();
        String imageUrl = uploadImage(multipartFile);
        family.addImage(imageUrl);
        return new FamilyImageResponse(imageUrl);
    }

    @Transactional
    public void deleteFamilyImage(final DeleteFamilyimageRequest deleteFamilyimageRequest) {
        Family family = authService.getLoginUser().getFamily();
        family.deleteImage(deleteFamilyimageRequest.getFamilyImage());
        s3ImageComponent.deleteImage(deleteFamilyimageRequest.getFamilyImage());
    }

    private String uploadImage(final MultipartFile multipartFile) {
        return s3ImageComponent.uploadImage("family-image", multipartFile);
    }

    @Transactional
    public InviteCodeResponse generateInviteCode() {
        Family family = authService.getLoginUser().getFamily();
        if(!validateInviteCodeNotExists(family.getInvite())) {
            return new InviteCodeResponse(family.getInvite().getCode());
        }

        String code = generateRandomInviteCode();
        family.organizeInvite(code);
        return new InviteCodeResponse(code);
    }

//    private void validateInviteCodeNotExists(final Invite invite) {
//        if (isValidCode(invite)) {
//            throw new InviteCodeExistException();
//        }
//        invite.delete();
//    }
    private Boolean validateInviteCodeNotExists(final Invite invite) {
        if (isValidCode(invite)) return Boolean.FALSE;
        invite.delete();
        return Boolean.TRUE;
    }
}
