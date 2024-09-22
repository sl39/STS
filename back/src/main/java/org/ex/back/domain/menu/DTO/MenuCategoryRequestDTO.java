package org.ex.back.domain.menu.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
//메뉴 카테고리 요청값
public class MenuCategoryRequestDTO {
    //카테고리 제목
    private String subject;
}
