package org.ex.back.domain.cart.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@Data
//장바구니 응답값
public class CartResponseDTO {

    //유저별 장바구니 식별 기본키
    private Integer cart_pk;

    //가게 조회를 위한 기본키
    private Integer store_pk;

    //가게 이름
    private String store_name;

    //장바구니 테이블 넘버
    private String tableNum;

    //장바구니 총 금액
    private Integer cartTotalPrice;

    //장바구니 안에 메뉴 정보(아이템pk, 메뉴pk, 등등 )
    private List<CartItemDTO> cartItem;

    public CartResponseDTO(Integer cartPk, Integer storePk, String storeName, String tableNumber, Integer totalPrice, List<CartItemDTO> collect) {
        this.cart_pk = cartPk;
        this.store_pk = storePk;
        this.store_name = storeName;
        this.tableNum = tableNumber;
        this.cartTotalPrice = totalPrice;
        this.cartItem = collect;
    }

    //선택된 옵션 아이템 리스트 문자열 -> itemList에 들어가있음
    //private String optionItemList;
}
