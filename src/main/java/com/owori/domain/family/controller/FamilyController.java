package com.owori.domain.family.controller;

import com.owori.domain.family.dto.request.AddMemberRequest;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/families")
public class FamilyController {
    private final FamilyService familyService;

    @PostMapping
    public ResponseEntity<InviteCodeResponse> saveFamily(@RequestBody FamilyRequest familyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(familyService.saveFamily(familyRequest));
    }

    @PostMapping("/members")
    public ResponseEntity<Void> saveFamilyMember(@RequestBody AddMemberRequest addMemberRequest) {
        familyService.addMember(addMemberRequest);
        return ResponseEntity.ok().build();
    }
}
