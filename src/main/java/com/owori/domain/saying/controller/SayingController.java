package com.owori.domain.saying.controller;

import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.service.SayingService;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
