package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindAllStoryGroupResponse {
    // todo: 다음 버전 배포 시 삭제

    private List<FindAllStoryResponse> stories;
    private Boolean hasNext;
}
