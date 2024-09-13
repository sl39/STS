package org.ex.back.domain.menu.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//옵션아이템 리스트
public class OptionItemList {
    //옵션 아이템 기본키
    private Integer option_item_pk;

    //옵션아이템 이름
    private String name;

    //옵션 아이템 가격
    private Integer extraPrice;
}

