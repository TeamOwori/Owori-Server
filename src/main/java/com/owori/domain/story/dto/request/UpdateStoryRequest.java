package com.owori.domain.story.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoryRequest {
    @NotNull
    private UUID storyId;

    @NotNull(message = "startDate는 필수 입력 값입니다.")
    @PastOrPresent(message = "startDate에 미래의 날짜를 입력할 수 없습니다.")
    private LocalDate startDate;

    @NotNull(message = "endDate는 필수 입력 값입니다.")
    private LocalDate endDate;

    @NotBlank(message = "title는 필수 입력 값입니다.")
    private String title;

    @Size(min = 1, max = 500)
    private String content;

    private List<UUID> imagesId;
}
