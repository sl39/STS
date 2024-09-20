package org.ex.back.domain.store.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreDTO {

    private Integer storePk; // 매장 PK
    private Integer ownerPk; // OwnerEntity의 PK를 포함
    private List<String> storeImages; // 이미지 URL 리스트
    private String storeName;
    private String address;
    private String phone;
    private String operatingHours; // 영업시간
    private String storeState; // 영업 상태
    private Boolean isOpen; // 영업 중 여부
    private Double lat; // 위도
    private Double lng; // 경도
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime deletedAt; // 삭제 시간
    private Double distance; // 거리 (선택적)
}
