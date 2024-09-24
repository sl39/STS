package org.ex.back.domain.cart.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//장바구니 메뉴 정보
public class CartItemList {
    //아이템별 기본키
    private Integer cart_item_pk;

    //선택된 메뉴 기본키
    private Integer menu_pk;

    //선택된 옵션 아이템들 리스트 문자열 값
    private String optionItemList;

    //선택된 메뉴 수량
    private Integer menuCount;

    //선택된 메뉴 가격 합
    private Integer totalPrice;
}
