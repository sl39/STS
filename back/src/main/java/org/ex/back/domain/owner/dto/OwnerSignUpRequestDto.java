package org.ex.back.domain.owner.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ex.back.domain.owner.model.OwnerEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerSignUpRequestDto {

    //판매자 정보
    private String id;
    private String password;
    private String email;
    private String BRN;
    private String ownerName;
    private String ownerPhone;
    private String bankAccount;

    //가게 정보



    @Builder
    public OwnerEntity toEntity(){
        return OwnerEntity.builder()
                .id(id)
                .password(password)
                .email(email)
                .BRN(BRN)
                .name(ownerName)
                .phone(ownerPhone)
                .bankAccount(bankAccount)
                .build();
    }
}
