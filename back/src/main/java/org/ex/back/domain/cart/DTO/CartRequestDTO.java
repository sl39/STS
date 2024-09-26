package org.ex.back.domain.cart.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
//장바구니 요청값
public class CartRequestDTO {
    //카트pk
    //private Integer cartId;

    //가게 pk
    private Integer store_pk;

    //가게 이름 (store_pk로 백에서 조회할수없이서 필요없음)
    //private String store_name;

    //선택된 메뉴 기본키
    private Integer menu_pk;

    //선택된 메뉴 수량 (요청 들어올때마다 하나씩인데 굳이 받을 필요가 없음)
    //private Integer menuCount;

    //메뉴가 추가된 테이블 번호 (프론트에서 만들어줘)
    private String tableNumber;

    //추가한 옵션 정보 (프론트에서 만들어줘)
    private String optionItemList;

    //장바구니 안에 메뉴 정보(가게이름, 아이템, 메뉴이름, 카운트, 등등 ) -> 이건 내가 묶어서 보내줄 것이므로 필요가 없다
    //private List<CartItemDTO> cartItem;

    //추가한 옵션들 가격합 (프론트에서 선택된 옵션 값들 더해줘)
    private Integer totalExtraPrice;

}
