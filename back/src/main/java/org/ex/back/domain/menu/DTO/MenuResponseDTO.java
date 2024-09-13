package org.ex.back.domain.menu.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//리턴해줄 데이터 타입 정의
@Getter
@NoArgsConstructor
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

    //메뉴 옵션 기본키
    private Integer menu_option_pk;

    //옵션 제목
    private String opSubject;

    //옵션 최소 선택 수
    private Integer minCount;

    //옵션 최대 선택수
    private Integer maxCount;

    //옵션 리스트
    private List<OptionItemList> optionItem;
}
