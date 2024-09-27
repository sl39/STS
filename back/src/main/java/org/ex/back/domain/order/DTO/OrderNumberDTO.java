package org.ex.back.domain.order.DTO;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNumberDTO {
    private String order_pk;
    private LocalDateTime orderedAt;
    private List<OrderItemCheckDTO> orderItems;
    private Integer totalPrice;
    private String paymentType;

}
