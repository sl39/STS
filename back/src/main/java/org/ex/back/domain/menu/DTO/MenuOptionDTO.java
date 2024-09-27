package org.ex.back.domain.menu.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MenuOptionDTO {
    private Integer menu_pk;

    private Integer menu_option_pk;

    private String opSubject;

    private Integer minCount;

    private Integer maxCount;

    public MenuOptionDTO(Integer menuPk, Integer menuOptionPk, String subject, Integer maxCount, Integer minCount, List<OptionItemDTO> optionItemDTOS) {
        menu_pk = menuPk;
        this.menu_option_pk = menuOptionPk;
        this.opSubject = subject;
        this.maxCount = maxCount;
        this.minCount = minCount;
    }


}
