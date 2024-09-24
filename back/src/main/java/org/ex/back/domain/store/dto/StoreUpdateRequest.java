package org.ex.back.domain.store.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreUpdateRequest {
    private String storeName;
    private String address;
    private String phone;
    private String operatingHours;
    private String storeState;
    private Boolean isOpen;
    private Double lat;
    private Double lng;
    private List<Integer> categoryIds; // 카테고리 ID 목록

}
