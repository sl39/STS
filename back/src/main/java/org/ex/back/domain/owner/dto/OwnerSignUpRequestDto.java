package org.ex.back.domain.owner.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ex.back.domain.owner.model.OwnerEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerSignUpRequestDto {

    //판매자 정보
    private String id;
    private String password;
    private String email;
    private String businessNumber;
    private String ownerName;
    private String ownerPhone;
    private String bankName;
    private String bankAccount;

    @Builder
    public OwnerEntity toEntity(){
        return OwnerEntity.builder()
                .id(id)
                .password(password)
                .email(email)
                .businessNumber(businessNumber)
                .name(ownerName)
                .phone(ownerPhone)
                .bankName(bankName)
                .bankAccount(bankAccount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
