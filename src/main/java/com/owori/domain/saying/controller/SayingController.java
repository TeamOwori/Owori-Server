package com.owori.domain.saying.controller;

import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.service.SayingService;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<IdResponse<UUID>> addSaying(@RequestBody AddSayingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sayingService.addSaying(request));
    }

    /**
     * 서로에게 한마디를 수정합니다.
     * @param sayingId 수정할 서로에게 한마디 id 입니다.
     * @param request 수정된 서로에게 한마디 정보입니다.
     * @return 수정된 서로에게 한마디의 id가 반환됩니다.
     */
    @PatchMapping("/update")
    public ResponseEntity<IdResponse<UUID>> updateSaying(@RequestParam UUID sayingId, @RequestBody UpdateSayingRequest request) {
        return ResponseEntity.ok(sayingService.updateSaying(sayingId, request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<IdResponse<UUID>> deleteSaying(@RequestParam UUID sayingId) {
        return ResponseEntity.ok(sayingService.deleteSaying(sayingId));
    }
}
