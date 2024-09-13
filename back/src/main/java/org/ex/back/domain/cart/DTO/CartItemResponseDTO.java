package org.ex.back.domain.cart.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
//장바구니 응답값
public class CartItemResponseDTO {
    //가게 조회를 위한 기본키
    private Integer store_pk;

    //가게 이름
    private String storeName;

    //유저별 장바구니 식별 기본키
    private Integer cart_pk;

    //장바구니 테이블 넘버
    private String tableNum;

    //장바구니 총 금액
    private Integer cartTotalPrice;

    //장바구니 안에 메뉴 정보
    private List<CartItemList> cartItem;
}
