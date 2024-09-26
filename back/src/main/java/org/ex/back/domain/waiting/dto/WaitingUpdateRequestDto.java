package org.ex.back.domain.waiting.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingUpdateRequestDto {

    private String waitingState; // 대기, 입장, 취소
}
