package org.ex.back.domain.store.dto;

import lombok.Data;

@Data
public class StoreCategoryDTO {
    private Integer storeCategoryPk; // 카테고리 PK
    private String categoryName; // 카테고리 이름
    // 필요한 다른 필드 추가 가능
}
