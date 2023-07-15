package com.owori.domain.story.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
public class SearchStoryRequest {

    @Size(message = "검색어를 2글자 이상 입력해주세요.", min = 2)
    private String keyword;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastViewed;
}
