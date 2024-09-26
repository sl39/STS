package org.ex.back.domain.order.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class StoreOrderListResponseDTO {
    private Integer store_pk;
    private String order_pk;
    private String tableNumber;
    private Integer totalPrice;
    private String paymentType;
    private LocalDateTime orderedAt;
    private List<OrderItemCheckDTO> orderItems;

}
