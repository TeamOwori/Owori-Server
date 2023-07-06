package com.owori.domain.story.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddStoryRequest {
    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String contents;
    private List<UUID> images;
}
