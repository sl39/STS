package org.ex.back.domain.menu.Service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.menu.DTO.MenuOptionDTO;
import org.ex.back.domain.menu.Repository.MenuOptionRepository;
import org.ex.back.domain.menu.Repository.MenuRepository;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Service
@Slf4j
public class OptionService {
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;

    //메뉴 옵션 생성 (옵션 생성할 메뉴 id, 생성할 옵션 정보 )
    public MenuOptionDTO createdOption (int menuId, MenuOptionDTO request){

        //받은 menu_pk로 옵션을 생성할 메뉴 정보 가져오기
        Optional<MenuEntity> menuEntity = menuRepository.findById(menuId);
        if(menuEntity.isPresent()){
            MenuEntity menu = menuEntity.get();

            //옵션 엔티티 정보를 request 받은 정보로 변환
            MenuOptionEntity menuOptionEntity = new MenuOptionEntity();
            menuOptionEntity.setSubject(request.getOpSubject());
            menuOptionEntity.setMinCount(request.getMinCount());
            menuOptionEntity.setMaxCount(request.getMaxCount());

            menuOptionRepository.save(menuOptionEntity);

            // 이제 저거를 menu리스트에 추가
            menu.getMenuOptions().add(menuOptionEntity);

            menuRepository.save(menu);

            //만들어진 엔티티를 DTO 타입으로 반환
            MenuOptionDTO response = new MenuOptionDTO();
            response.setMenu_pk(menu.getMenu_pk());
            response.setMenu_option_pk(menuOptionEntity.getMenu_option_pk());
            response.setOpSubject(menuOptionEntity.getSubject());
            response.setMinCount(menuOptionEntity.getMinCount());
            response.setMaxCount(menuOptionEntity.getMaxCount());

            return response;
        }
        else{
            //해당 메뉴가 존재하지 않을 시 에러
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
    }

    //옵션 수정
   // public MenuOptionDTO updateMenuOption(int menuId, MenuOptionDTO request){

   // }


}
