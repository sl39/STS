package org.ex.back.domain.waiting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WaitingCreateRequestDto {

    private String phone;
    private Integer headCount;
}
