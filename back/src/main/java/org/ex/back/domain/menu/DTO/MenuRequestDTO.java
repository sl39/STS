package org.ex.back.domain.menu.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

//HTTP 요청 요청본문 데이터를 받아올 데이터 타입 정의
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

    //메뉴 옵션값
    private Integer menu_option_pk;

    //메뉴 옵션 제목
    private String opSubject;

    //최소 선택 갯수
    private Integer minCount;

    //최대 선택 갯수
    private Integer maxCount;

    //옵션 아이템 리스트
    private List<OptionItemList> optionItem;
}
