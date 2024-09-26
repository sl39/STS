package org.ex.back.domain.order.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCheckDTO {
    private String menuName;
    private Integer menuCount;
    private String optionltemList;

}
