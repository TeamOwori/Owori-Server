package com.owori.domain.saying.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSayingRequest {
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    @NotNull(message = "빈 리스트 형태로 보내주세요")
    private List<UUID> tagMembersId;
}
