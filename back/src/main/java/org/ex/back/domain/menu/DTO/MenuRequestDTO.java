package org.ex.back.domain.menu.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
//메뉴 요청값
public class MenuRequestDTO {
    //메뉴의 카테고리 값
    private Integer category_pk;

    //카테고리 값의 제목
    private String subject;

    //메뉴 이름값
    private String name;

    //메뉴 가격
    private Integer price;

    //메뉴 설명
    private String description;

    //메뉴 이미지
    private String imageURL;

    //인게메뉴 여부
    private Boolean isBestMenu;

    //주류 여부(메뉴가 술인지 아닌지)
    private Boolean isAlcohol;

    //옵션들을 받을 옵션 리스트 -> 일단 생성할때 optionList = null로 리퀘스트
    private List<OptionsDTO> options;
}
