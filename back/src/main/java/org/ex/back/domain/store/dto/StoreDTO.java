package org.ex.back.domain.store.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.ex.back.domain.store.model.StoreImageEntity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreDTO {

    private Integer storePk;
    private Integer ownerPk; // OwnerEntity의 PK를 포함
    private List<String> storeImageUrls; // 이미지 URL 리스트로 변경
    private String storeName;
    private String address;
    private String phone;
    private String operatingHours;
    private String storeState;
    private Boolean isOpen;
    private Double lat;
    private Double lng;
    private LocalDateTime createdAt = LocalDateTime.now(); // 기본값 설정
    private LocalDateTime deletedAt;
    private Double distance;
}
