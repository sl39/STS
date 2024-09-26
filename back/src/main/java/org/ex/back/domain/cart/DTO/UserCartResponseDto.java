package org.ex.back.domain.cart.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Data
public class UserCartResponseDto {

    private Integer userCartPk;
}
