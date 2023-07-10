package com.owori.domain.saying.controller;

import com.owori.domain.saying.service.SayingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saying")
public class SayingController {
    private final SayingService sayingService;
}
