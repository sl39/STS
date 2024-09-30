package org.ex.back.domain.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemTotalDTO {
    // 주문번호 조회 DTO
    private String menuName;
    private Integer menuCount;
    private String optionItemList;
    private Integer menuPrice;
    private Integer menuExtraPrice;
}
