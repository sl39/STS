package org.ex.back.domain.menu.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ex.back.domain.menu.model.OptionItemEntity;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OptionsDTO {
    private Integer id;

    private String opSubject;

    private Integer minCount;

    private Integer maxCount;

    private List<OptionItemDTO> optionItems;

    //옵션 엔티티 -> 옵션 DTO 변환을 위한 생성자(stream API)
    public OptionsDTO(Integer menuOptionPk, String subject, Integer maxCount, Integer minCount, List<OptionItemDTO> optionItemDTOS) {
        this.id = menuOptionPk;
        this.opSubject = subject;
        this.maxCount = maxCount;
        this.minCount = minCount;
        this.optionItems = optionItemDTOS;
    }
}
