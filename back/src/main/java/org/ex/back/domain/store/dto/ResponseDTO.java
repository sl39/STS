package org.ex.back.domain.store.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO {
    private Integer ownerPk;
    private Boolean hasStore;
    private Integer storePk; // 추가된 필드
}
