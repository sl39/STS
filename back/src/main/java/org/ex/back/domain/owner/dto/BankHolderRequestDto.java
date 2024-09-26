package org.ex.back.domain.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankHolderRequestDto {

    private String bank_code;
    private String bank_num;
}
