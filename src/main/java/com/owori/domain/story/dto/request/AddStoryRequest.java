package com.owori.domain.story.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddStoryRequest {
    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String contents;
}
