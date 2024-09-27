package org.ex.back.domain.order.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserDetailDTO {
    private String order_pk;
    private String storeName;
    private String storeAddress;
    private Integer totalPrice;
    private LocalDateTime orderedAt;
    private String paymentType;
    private List<OrderItemCheckDTO> orderItems;
}
