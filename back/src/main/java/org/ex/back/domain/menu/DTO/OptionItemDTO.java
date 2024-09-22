package org.ex.back.domain.menu.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OptionItemDTO {
    private Integer id;

    private String name;

    private Integer extraPrice;

    public OptionItemDTO(Integer id, String name, Integer extraPrice) {
        this.id = id;
        this.name = name;
        this.extraPrice = extraPrice;
    }
    /*
    //로그 출력을 위한 toString메소드
    @Override
    public String toString() {
        return "OptionItemDTO{" +
                "option_item_pk=" + id +
                ", name='" + name + '\'' +
                ", extraPrice=" + extraPrice +
                '}';
    }
     */

}
