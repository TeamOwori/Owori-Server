package com.owori.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberColorResponse {
    private boolean red;
    private boolean yellow;
    private boolean green;
    private boolean pink;
    private boolean skyblue;
    private boolean blue;
    private boolean purple;
}
