package org.ex.back.domain.store.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {
    private Integer storePk; // 매장 ID
    private String storeName; // 매장 이름
    private String address; // 매장 주소 
    private Double lat; // 위도
    private Double lng; // 경도
    private String menu; // 매장 메뉴
    private List<StoreCategoryDTO> storeCategories; // 매장 카테고리
    private Integer ownerId; // 매장 소유자 ID
    private String Phone; // 소유자 연락처
    private String operatingHours; // 운영 시간
    private String storeState; //영업, 휴업, 폐업
    private Boolean isOpen; //영업중, 영업종료
}
