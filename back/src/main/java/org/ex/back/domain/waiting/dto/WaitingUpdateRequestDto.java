package org.ex.back.domain.waiting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WaitingUpdateRequestDto {

    private String waitingState; // 대기, 입장, 취소
}
