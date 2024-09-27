package org.ex.back.domain.menu.Service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.menu.DTO.MenuRequestDTO;
import org.ex.back.domain.menu.DTO.MenuResponseDTO;
import org.ex.back.domain.menu.DTO.OptionItemDTO;
import org.ex.back.domain.menu.DTO.OptionsDTO;
import org.ex.back.domain.menu.Repository.MenuCategoryRepository;
import org.ex.back.domain.menu.Repository.MenuOptionRepository;
import org.ex.back.domain.menu.Repository.MenuRepository;
import org.ex.back.domain.menu.Repository.OptionItemRepository;
import org.ex.back.domain.menu.model.MenuCategoryEntity;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.ex.back.domain.menu.model.OptionItemEntity;
import org.ex.back.domain.store.repository.StoreRepository;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@Service
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final OptionItemRepository optionItemRepository;
    private final MenuCategoryService menuCategoryService;
    private final MenuCategoryRepository menuCategoryRepository;

    //이게 먼저 사용됨
    //옵션 아이템 엔티티 -> 옵션 아이템 DTO 변환
    private List<OptionItemDTO> convertItemDTO(List<OptionItemEntity> optionItems){
        log.info("Converting OptionItemDTO from OptionItemEntity2222: {}", optionItems);

        List<OptionItemDTO> optionsItemsDTO = optionItems.stream()
                .map(item -> new OptionItemDTO(
                        item.getOption_item_pk(),
                        item.getName(),
                        item.getExtraPrice()
                ))
                .collect(Collectors.toList());
        log.info("Converting OptionItemDTO from OptionItemEntity: {}", optionsItemsDTO);

        return optionsItemsDTO;
    }

    //옵션 엔티티 -> 옵션DTO 변환
    private List<OptionsDTO> convertOptionsDTO(List<MenuOptionEntity> optionItems){
        List<OptionsDTO> optionDTO = optionItems.stream()
                .map(options -> new OptionsDTO(
                        options.getMenu_option_pk(),
                        options.getSubject(),
                        options.getMaxCount(),
                        options.getMinCount(),
                        convertItemDTO(options.getOptionItems())
                ))
                .collect(Collectors.toList());
        //log.info("Converting OptionsDTO from MenuOptionEntity: {}", optionDTO);

        return optionDTO;
    }

    //옵션 DTO -> 옵션 엔티티 변환
    private List<MenuOptionEntity> convertMenuOptionEntity(List<OptionsDTO> optionDTO){
      List<MenuOptionEntity> menuOptionEntity = optionDTO.stream()
              .map(option -> new MenuOptionEntity(
                      option.getOption_pk(),
                      option.getOpSubject(),
                      option.getMaxCount(),
                      option.getMinCount(),
                      convertOptionItemEntity(option.getOptionItems())
              ))
              .collect(Collectors.toList());
     return menuOptionEntity;
    }

    //옵션 아이템 DTO -> 옵션 아이템 엔티티 변환
    private List<OptionItemEntity> convertOptionItemEntity(List<OptionItemDTO> optionItemDTO){
        List<OptionItemEntity> optionItemEntity = optionItemDTO.stream()
                .map(item -> new OptionItemEntity(
                        item.getOption_item_pk(),
                        item.getName(),
                        item.getExtraPrice()
                ))
                .collect(Collectors.toList());
        return optionItemEntity;
    }

    //메뉴 생성(request, 스토어 아이디)
    @Transactional
    public MenuResponseDTO createdMenu(int storeId, MenuRequestDTO request) {

        Optional<StoreEntity> store = storeRepository.findById(storeId);

        if (store.isPresent()) {
            StoreEntity storeEntity = store.get();

            //가게에 존재하는 카테고리 정보를 받아와서 현재 메뉴의 카테고리로 바꿔줘야함
            //request에 존재하는 카테고리 id 와 일치하는 카테고리 리포지터리 id를 가져와서 메뉴에 넣음
            Optional<MenuCategoryEntity> menuCategory = menuCategoryRepository.findById(request.getCategory_pk());

            if (menuCategory.isPresent()) {
                //request의 카테고리pk와 일치하는 카테고리 객체 생성
                MenuCategoryEntity categoryEntity = menuCategory.get();

                //request로 받은 옵션 리스트{{이름, 최대.최소 {아이템 리스트}}} -> 이름 최대 최소 저장 -> 아이템 리스트 저장
                //요청 DTO에서 옵션리스트{pk, 이름, {최대최소/ 아이템 리스트}} 가져오기
                List<OptionsDTO> optionsList = request.getOptions();

                if (optionsList != null) {
                    //MenuOptionEntity 리스트를 저장할 리스트
                    List<MenuOptionEntity> menuOptionEntities = new ArrayList<>();

                    // 부모 엔티티 먼저 저장하기
                    //부모 엔티티 메뉴 옵션 null이 아닌 빈 리스트 하나 넣어두기
                    MenuEntity menu = MenuEntity.builder()
                            .menuOptions(new ArrayList<>())
                            .build();

                    //리포지 터리 저장으로 메뉴 기본키 생성 -> 나중에 한꺼번에 저장하는걸로
                    //menu = menuRepository.save(menu);
                    log.info("menu 엔티티 : {}", menu);

                    //{이름, 최대최소, {아이템 리스트}}
                    for (OptionsDTO optionsDTO : optionsList) {
                        //옵션 리스트별 이름, 최대최소 변경
                        MenuOptionEntity menuOptionEntity = new MenuOptionEntity();

                        menuOptionEntity.setSubject(optionsDTO.getOpSubject());
                        menuOptionEntity.setMaxCount(optionsDTO.getMaxCount());
                        menuOptionEntity.setMinCount(optionsDTO.getMinCount());
                        log.info("menuOptionEntity 엔티티 : {}", menuOptionEntity);

                        //아이템 리스트 리스트로 가져오기
                        List<OptionItemDTO> optionItemDTOS = optionsDTO.getOptionItems();
                        // 아이템 리스트를 메뉴 옵션 엔티티에 저장하기 위한 새로운 리스트
                        List<OptionItemEntity> optionItemEntities = new ArrayList<>();

                        //옵션 아이템 리스트를 옵션 아이템 엔티티에 저장하기
                        for (OptionItemDTO optionItemDTO : optionItemDTOS) {
                            //아이템 리스트 (대.100 / 중.200 / 소.300) 차례대로 저장
                            OptionItemEntity optionItemEntity = new OptionItemEntity();
                            optionItemEntity.setName(optionItemDTO.getName());
                            optionItemEntity.setExtraPrice(optionItemDTO.getExtraPrice());

                            // 저장된 옵션 아이템을 리스트에 추가
                            optionItemEntities.add(optionItemEntity);
                        }
                        log.info("optionItem : {}", optionItemEntities);

                        //메뉴 옵션 엔티티에 만들어진 아이템 리스트 저장
                        menuOptionEntity.setOptionItems(optionItemEntities);
                        log.info("menuOptionEntity 개수 : {}", menuOptionEntity);

                        // 메뉴 옵션 엔티티 저장 후 PK 생성 (아이템 리스트도 Cascade 때문에 같이 저장됨 아이템 리스트 pk 생성)
                        //  -> 나중에 한꺼번에 저장(메뉴pk -> 메뉴 옵션 pk -> 옵션 아이템pk 순으로 cascade저장)
                        //menuOptionEntity = menuOptionRepository.save(menuOptionEntity);

                        // 저장된 메뉴 옵션을 리스트에 추가
                        menuOptionEntities.add(menuOptionEntity);
                        log.info("menuOption 개수 : {}", menuOptionEntities);
                    }

                    //엔티티값을 request 값으로 변경
                    menu = MenuEntity.builder()
                            .store(storeEntity)
                            .name(request.getName())
                            .description(request.getDescription())
                            .price(request.getPrice())
                            .imageUrl(request.getImageURL())
                            .isBestMenu(request.getIsBestMenu())
                            .isAlcohol(request.getIsAlcohol())
                            .menuCategory(categoryEntity)
                            .menuOptions(menuOptionEntities) //저장된 리스트 {이름, 최대최소, {아이템 리스트}}
                            .build();

                    //변경된 값을 repository에 저장
                    menu = menuRepository.save(menu);
                    log.info("Menu Options: {}", menu.getMenuOptions());

                    // response 값으로 변경
                    MenuResponseDTO response = MenuResponseDTO.builder()
                            .category_pk(menu.getMenuCategory().getMenu_category_pk())
                            .subject(menu.getMenuCategory().getSubject())
                            .menu_pk(menu.getMenu_pk())
                            .name(menu.getName())
                            .description(menu.getDescription())
                            .price(menu.getPrice())
                            .imageURL(menu.getImageUrl())
                            .isBestMenu(menu.getIsBestMenu())
                            .isAlcohol(menu.getIsAlcohol())
                            //메뉴 옵션 엔티티 타입 리스트 -> 옵션DTO 타입 리스트 로 변환
                            .options(convertOptionsDTO(menu.getMenuOptions()))
                            .build(); // 빌더 패턴으로 최종 객체 생성

                    return response;

                } else {
                    //엔티티값을 request 값으로 변경
                    MenuEntity menu = MenuEntity.builder()
                            .store(storeEntity)
                            .name(request.getName())
                            .description(request.getDescription())
                            .price(request.getPrice())
                            .imageUrl(request.getImageURL())
                            .isBestMenu(request.getIsBestMenu())
                            .isAlcohol(request.getIsAlcohol())
                            .menuCategory(categoryEntity)
                            .build();

                    //변경된 값을 repository에 저장
                    menu = menuRepository.save(menu);

                    // response 값으로 변경
                    MenuResponseDTO response = MenuResponseDTO.builder()
                            .category_pk(menu.getMenuCategory().getMenu_category_pk())
                            .subject(menu.getMenuCategory().getSubject())
                            .menu_pk(menu.getMenu_pk())
                            .name(menu.getName())
                            .description(menu.getDescription())
                            .price(menu.getPrice())
                            .imageURL(menu.getImageUrl())
                            .isBestMenu(menu.getIsBestMenu())
                            .isAlcohol(menu.getIsAlcohol())
                            .build(); // 빌더 패턴으로 최종 객체 생성

                    return response;

                }
            }
            //카테고리가 없으면 예외 반환
            else {
                throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        }
        //스토어가 없으면 예외 반환
        else {
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }
    }

    //메뉴 리스트 조회(스토어 id )
    public List<MenuResponseDTO> getMenuList(int id){
        //스토어 존재 여부 확인
        Optional<StoreEntity> store = storeRepository.findById(id);

        if(store.isPresent()){
            //리스트 안에 스토어id 값과 일치하는 메뉴 리스트 담기
            List<MenuEntity> menuList = menuRepository.findByStore(store.get());

            //menuList 를 ResponseDTO타입 리스트로 변환 streamAPI 사용필요
            List<MenuResponseDTO> response = menuList.stream()
                    .map(menu -> new MenuResponseDTO(
                            menu.getMenu_pk(),
                            menu.getMenuCategory().getMenu_category_pk(),
                            menu.getMenuCategory().getSubject(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getDescription(),
                            menu.getImageUrl(),
                            menu.getIsBestMenu(),
                            menu.getIsAlcohol()
                    ))
                    .collect(Collectors.toList());
            return response;
        }
        else{
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }

    }

    //메뉴 상세 조회(메뉴  id) -> 메뉴 리스트에서 상세 조회가 이루어지기 때문에 스토어 ID가 필요없음
   public MenuResponseDTO getMenu(int menuId){

        //메뉴id로 메뉴정보 가져오기
        Optional<MenuEntity> menu = menuRepository.findById(menuId);
        if(menu.isPresent()){
            //response로 변환하기 위한 엔티티 객체
            MenuEntity menuEntity = menu.get();
            MenuResponseDTO response = MenuResponseDTO.builder().
                    category_pk(menuEntity.getMenuCategory().getMenu_category_pk())
                    .subject(menuEntity.getMenuCategory().getSubject())
                    .menu_pk(menuEntity.getMenu_pk())
                    .name(menuEntity.getName())
                    .price(menuEntity.getPrice())
                    .description(menuEntity.getDescription())
                    .imageURL(menuEntity.getImageUrl())
                    .isBestMenu(menuEntity.getIsBestMenu())
                    .isAlcohol(menuEntity.getIsAlcohol())
                    .options(convertOptionsDTO(menuEntity.getMenuOptions()))
                    .build();
            return response;
        }
        else{
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
    }

    //메뉴 수정(menuId받음, 스토어 내에서 이루어지기 때문에 스토어 Id 필요 X)
    public MenuResponseDTO putMenu(int menuId, MenuRequestDTO request) {
        //메뉴 id로 받아온 값 찾아서 넣기
        Optional<MenuEntity> menu = menuRepository.findById(menuId);

        if (menu.isPresent()) {
            Optional<MenuCategoryEntity> menuCategory = menuCategoryRepository.findById(request.getCategory_pk());

            if (menuCategory.isPresent()) {
                //request의 카테고리pk와 일치하는 카테고리 객체 생성
                MenuCategoryEntity categoryEntity = menuCategory.get();

                MenuEntity menuEntity = menu.get();

                log.info("menuEntity11111 : {}", menuEntity);

                menuEntity = menuEntity.builder()
                        //수정후 메뉴 pk를 유지하기 위해 새로 생성되지 않도록 기존 pk 로 설정
                        .menu_pk(menuEntity.getMenu_pk())
                        .menuCategory(categoryEntity)
                        .name(request.getName())
                        .isBestMenu(request.getIsBestMenu())
                        .isAlcohol(request.getIsAlcohol())
                        .price(request.getPrice())
                        .imageUrl(request.getImageURL())
                        .description(request.getDescription())
                        .menuOptions(convertMenuOptionEntity(request.getOptions()))
                        .build();
                menuRepository.save(menuEntity);

                log.info("menuEntity2222 : {}", menuEntity);

                //ResponseDTO로 변환
                MenuResponseDTO response = MenuResponseDTO.builder()
                        .category_pk(menuEntity.getMenuCategory().getMenu_category_pk())
                        .subject(menuEntity.getMenuCategory().getSubject())
                        .menu_pk(menuEntity.getMenu_pk())
                        .name(menuEntity.getName())
                        .price(menuEntity.getPrice())
                        .description(menuEntity.getDescription())
                        .imageURL(menuEntity.getImageUrl())
                        .isBestMenu(menuEntity.getIsBestMenu())
                        .isAlcohol(menuEntity.getIsAlcohol())
                        //메뉴 옵션 엔티티 타입 리스트 -> 옵션DTO 타입 리스트 로 변환
                        .options(convertOptionsDTO(menuEntity.getMenuOptions()))
                        .build();

                return response;
            }
            else {
                throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        }else {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
    }

    //메뉴 삭제 (스토어 안에서 이루어지므로 storeID 필요 X)
    public void deleteMenu(int menuId) {
        //메뉴 id로 받아온 값 찾아서 넣기
        Optional<MenuEntity> menu = menuRepository.findById(menuId);
        if(menu.isPresent()){
            MenuEntity menuEntity = menu.get();
            menuRepository.delete(menuEntity);
        }
        else{
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
    }
}
