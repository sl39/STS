package org.ex.back.domain.order.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class StoreOrderListResponseDTO {
    Integer store_pk;
    String order_pk;
    String tableNumber;
    Integer totalPrice;
    String paymentType;
    Boolean isPaidAll;
    Boolean isClear;
    LocalDateTime orderedAt;



}
