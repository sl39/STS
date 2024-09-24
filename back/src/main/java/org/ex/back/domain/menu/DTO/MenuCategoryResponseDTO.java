package org.ex.back.domain.menu.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
//카테고리 응답 값
public class MenuCategoryResponseDTO {
    //카테고리 기본키
    private Integer category_pk;

    //카테고리 이름
    private String subject;
}


