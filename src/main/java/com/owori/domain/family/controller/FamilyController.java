package com.owori.domain.family.controller;

import com.owori.domain.family.dto.request.AddMemberRequest;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.service.FamilyService;
import com.owori.global.dto.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/families")
public class FamilyController {
    private final FamilyService familyService;

    /**
     * 가족 생성 컨트롤러입니다.
     * 가족을 로그인한 유저를 포함하며 생성해 저장합니다. 초대코드를 response합니다.
     * @param familyRequest 가족 그룹 명을 가지는 dto 입니다.
     * @return 초대 코드를 가지는 response dto 입니다.
     */
    @PostMapping
    public ResponseEntity<InviteCodeResponse> saveFamily(@RequestBody @Valid FamilyRequest familyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(familyService.saveFamily(familyRequest));
    }

    /**
     * 가족 초대 코드 검증 및 멤버 추가 컨트롤러입니다.
     * 초대 코드로 가족을 찾아 유효하면 멤버를 추가시킵니다.
     * @param addMemberRequest 초대 코드를 가지고 있는 dto 입니다.
     * @return 바디로 response 하는 정보는 없습니다.
     */
    @PostMapping("/members")
    public ResponseEntity<Void> addFamilyMember(@RequestBody @Valid AddMemberRequest addMemberRequest) {
        familyService.addMember(addMemberRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 가족 그룹 이름을 수정하는 컨트롤러입니다.
     * 가족 그룹 이름을 받아와 업데이트합니다.
     * @param familyRequest 업데이트할 가족 그룹 이름을 가집니다.
     * @return 바디로 response 하는 정보는 없습니다.
     */
    @PostMapping("/group-name")
    public ResponseEntity<Void> updateGroupName(@RequestBody @Valid FamilyRequest familyRequest) {
        familyService.updateGroupName(familyRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 가족 사진을 저장하는 컨트롤러입니다.
     * 가족 사진을 받아와 업로드 후 url을 response 합니다.
     * @param familyImage 가족 사진입니다.
     * @return 이미지의 url을 response 합니다.
     */
    @PostMapping("/images")
    public ResponseEntity<ImageResponse> saveFamilyImage(MultipartFile familyImage) {
        return ResponseEntity.status(HttpStatus.CREATED).body(familyService.saveFamilyImage(familyImage));
    }

    /**
     * 가족 초대 코드 재생성 컨트롤러입니다.
     * 가족 초대 코드가 유효하지 않을 때 재생성합니다.
     * @return 가족 초대 코드를 response 합니다.
     */
    @GetMapping("/code")
    public ResponseEntity<InviteCodeResponse> generateInviteCode() {
        return ResponseEntity.ok(familyService.generateInviteCode());
    }
}
