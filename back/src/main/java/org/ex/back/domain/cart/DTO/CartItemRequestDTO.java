package org.ex.back.domain.cart.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//장바구니 요청값
public class CartItemRequestDTO {
    //선택된 옵션아이템 리스트 문자열
    private String optionItemList;

    //선택된 옵션아이템 가격 총합
    private Integer totalExtraPrice;

    //선택된 메뉴 기본키
    private Integer menu_pk;

    //선택된 메뉴 가격
    private Integer price;

    //선택된 메뉴 수량
    private Integer menuCount;
}
