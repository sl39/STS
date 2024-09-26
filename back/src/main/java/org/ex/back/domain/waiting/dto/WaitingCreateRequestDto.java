package org.ex.back.domain.waiting.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingCreateRequestDto {

    private String phone;
    private Integer headCount;
}
