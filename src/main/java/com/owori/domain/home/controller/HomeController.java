package com.owori.domain.home.controller;

import com.owori.domain.home.dto.response.FindHomeResponse;
import com.owori.domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<FindHomeResponse> findHomeData() {
        return ResponseEntity.ok(homeService.findHomeData());
    }
}
