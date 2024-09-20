package org.ex.back.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateDTO {
    private String storeName; // 매장 이름
    private String address;    // 매장 주소
    private String phone;      // 매장 전화번호
    private String operatingHours; // 운영 시간
    private String storeState; // 영업 상태
    private Boolean isOpen;     // 영업 중 여부
    private Double lat;        // 위도
    private Double lng;        // 경도
    private List<Integer> categoryPks; // 카테고리 ID 리스트
    private List<String> storeImages; // 이미지 URL 리스트
}
