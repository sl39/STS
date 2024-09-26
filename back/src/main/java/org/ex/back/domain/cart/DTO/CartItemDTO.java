package org.ex.back.domain.cart.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//장바구니 메뉴 정보
public class CartItemDTO {
    //아이템별 기본키
    private Integer cart_item_pk;

    //선택된 메뉴 기본키
    private Integer menu_pk;

    //메뉴 이름
    private String menu_name;

    //선택된 옵션 아이템들 리스트 문자열 값
    private String optionItemList;

    //선택된 메뉴 수량(만들어진 것들을 프론트로 보내줘야함)
    private Integer menuCount;

    // 옵션 추가 금액 (프론트가 넣어서 보내야 할거)
    private Integer totalExtraPrice; //옵션으로 인해 추가된 금액

    //선택된 가격 총합 (메뉴금액+ 옵션 추가금액) X 수량 (내가 넣어서 보내야할거)
    private Integer totalPrice;

    //메뉴의 이미지
    private String ImageURL;

    public CartItemDTO(Integer cartItemPk, Integer menuPk, String name, String optionItemList, Integer menuCount, Integer totalPrice, String imageUrl) {
        this.cart_item_pk = cartItemPk;
        this.menu_pk = menuPk;
        this.menu_name = name;
        this.optionItemList = optionItemList;
        this.menuCount = menuCount;
        this.totalPrice = totalPrice;
        this.ImageURL = imageUrl;
    }

}
