package com.owori.domain.saying.controller;

import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.saying.dto.response.SayingIdResponse;
import com.owori.domain.saying.service.SayingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saying")
public class SayingController {
    private final SayingService sayingService;

    /**
     * 서로에게 한마디를 생성합니다.
     * @param request 서로에게 한마디 생성을 위한 dto 입니다.
     * @return 생성된 서로에게 한마디의 id가 반환됩니다.
     */
    @PostMapping
    public ResponseEntity<SayingIdResponse> addSaying(@RequestBody @Valid AddSayingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sayingService.addSaying(request));
    }

    /**
     * 서로에게 한마디를 수정합니다.
     * @param request 수정된 서로에게 한마디 정보입니다.
     * @return 수정된 서로에게 한마디의 id가 반환됩니다.
     */
    @PostMapping("/update")
    public ResponseEntity<SayingIdResponse> updateSaying(@RequestBody @Valid UpdateSayingRequest request) {
        return ResponseEntity.ok(sayingService.updateSaying(request));
    }

    /**
     * 서로에게 한마디를 삭제합니다.
     * @param sayingId 삭제할 서로에게 한마디 id 입니다.
     * @return 삭제된 서로에게 한마디의 id가 반환됩니다.
     */
    @DeleteMapping("/{sayingId}")
    public ResponseEntity<Void> deleteSaying(@PathVariable UUID sayingId) {
        sayingService.deleteSaying(sayingId);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그인 유저가 포함된 가족의 서로에게 한마디를 조회합니다.
     * @return 가족의 서로에게 한마디를 정보를 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<SayingByFamilyResponse>> findSayingByFamily(){
        return ResponseEntity.ok(sayingService.findSayingByFamily());
    }
}
