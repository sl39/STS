package org.ex.back.domain.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreFcmTokenDTO {
    private Integer storePk;
    private String fcmToken;
}
