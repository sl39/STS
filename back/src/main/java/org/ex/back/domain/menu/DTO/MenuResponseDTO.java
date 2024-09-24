package org.ex.back.domain.menu.DTO;

import lombok.*;
import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.ex.back.domain.menu.model.OptionItemEntity;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//메뉴 응답 값
public class MenuResponseDTO {
    // 카테고리 값
    private Integer category_pk;

    //키테고리 제목
    private String subject;

    //메뉴 기본키
    private Integer menu_pk;

    //메뉴 이름
    private String name;

    //메뉴 가격
    private Integer price;

    //메뉴설명
    private String description;

    //메뉴이미지
    private String imageURL;

    //인기메뉴 여부
    private Boolean isBestMenu;

    //주류 여부
    private Boolean isAlcohol;

    //모든 옵션 리스트(제목, 최대, 최소, 아이템 리스트)
    private List<OptionsDTO> options;

    //메뉴 엔티티 -> 메뉴리스폰스DTO 변환을 위한 생성자 (stream)
    public MenuResponseDTO(Integer menuPk, Integer menuCategoryPk, String subject, String name, Integer price, String description, String imageUrl, Boolean isBestMenu, Boolean isAlcohol) {
        this.menu_pk = menuPk;
        this.category_pk = menuCategoryPk;
        this.subject = subject;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageUrl;
        this.isBestMenu = isBestMenu;
        this.isAlcohol = isAlcohol;
    }
}