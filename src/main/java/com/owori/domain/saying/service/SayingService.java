package com.owori.domain.saying.service;

import com.owori.domain.saying.repository.SayingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SayingService {
    private final SayingRepository sayingRepository;
}
