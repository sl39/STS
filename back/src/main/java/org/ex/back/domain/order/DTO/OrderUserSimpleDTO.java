package org.ex.back.domain.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserSimpleDTO {
    private String order_pk;
    private Integer user_pk;
    private String storeName;
    private String storeAddress;
    private LocalDateTime orderedAt;
    private String storeImageUrl;
}
