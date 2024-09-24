package org.ex.back.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCategoryDTO {
    private Integer categoryPk; // 카테고리 ID
    private String subject; // 카테고리 이름
}
