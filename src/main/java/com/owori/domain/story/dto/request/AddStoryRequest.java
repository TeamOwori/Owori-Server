package com.owori.domain.story.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddStoryRequest {
    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String contents;
}
