package org.ex.back.domain.menu.Service;


import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.cart.model.CartItemEntity;
import org.ex.back.domain.cart.repository.CartItemRepository;
import org.ex.back.domain.menu.DTO.OptionsDTO;
import org.ex.back.domain.menu.Repository.MenuOptionRepository;
import org.ex.back.domain.menu.Repository.MenuRepository;
import org.ex.back.domain.menu.Repository.OptionItemRepository;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Service
@Slf4j
public class OptionService {
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final OptionItemRepository optionItemRepository;
    private final CartItemRepository cartItemRepository;

    //메뉴 옵션 생성 (옵션 생성할 메뉴 id, 생성할 옵션 정보 )
    public OptionsDTO createdOption (int menuId, OptionsDTO request){

        //받은 menu_pk로 옵션을 생성할 메뉴 정보 가져오기
        Optional<MenuEntity> menuEntity = menuRepository.findById(menuId);
        if(menuEntity.isPresent()){
            MenuEntity menu = menuEntity.get();

            //옵션 엔티티를 새로 만들어서 옵션 정보를 request 받은 정보로 변환
            MenuOptionEntity menuOptionEntity = new MenuOptionEntity();
            menuOptionEntity.setMenu(menu);

            menuOptionEntity.setSubject(request.getOpSubject());
            menuOptionEntity.setMinCount(request.getMinCount());
            menuOptionEntity.setMaxCount(request.getMaxCount());

            menuOptionRepository.save(menuOptionEntity);

            // 만들어진 엔티티를 메뉴의 리스트에 추가 및 저장
            menu.getMenuOptions().add(menuOptionEntity);

            menuRepository.save(menu);

            //옵션 엔티티를 DTO 타입으로 반환
            OptionsDTO response = new OptionsDTO();
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
    public OptionsDTO updateMenuOption(int optionId, OptionsDTO request){
        Optional<MenuOptionEntity> menuOptionEntity = menuOptionRepository.findById(optionId);
        if(menuOptionEntity.isPresent()){

            MenuOptionEntity menuOption = menuOptionEntity.get();
            menuOption.setMenu_option_pk(menuOption.getMenu_option_pk());
            menuOption.setSubject(request.getOpSubject());
            menuOption.setMinCount(request.getMinCount());
            menuOption.setMaxCount(request.getMaxCount());

            menuOptionRepository.save(menuOption);

            OptionsDTO response = new OptionsDTO();
            response.setMenu_option_pk(menuOption.getMenu_option_pk());
            response.setOpSubject(menuOption.getSubject());
            response.setMinCount(menuOption.getMinCount());
            response.setMaxCount(menuOption.getMaxCount());
            return response;
        }
        else{
            throw new CustomException(ErrorCode.OPTION_NOT_FOUND);
        }
    }

    //옵션 삭제
    //옵션을 삭제할때 관련 카트 아이템 삭제 -> 관련된 카트 아이템들 찾아오기
    //해당 옵션이 존재하는 메뉴를 찾아와서 연관된 카트 아이쳄을 삭제한다..
    @Transactional
    public void deleteMenuOption(int optionId){
        //옵션 아이디로 옵션 찾아오기
        Optional<MenuOptionEntity> menuOptionEntity = menuOptionRepository.findById(optionId);
        if(menuOptionEntity.isPresent()){
            //해당 옵션 가져오기
            MenuOptionEntity menuOption = menuOptionEntity.get();

            //해당 옵션을 가진 메뉴 가져오기
            MenuEntity menu = menuOption.getMenu();

            //메뉴를 통해 카트 아이템 가져오기
            List<CartItemEntity> cartItems = cartItemRepository.findByMenu( menu );

            //연관된 카트 아이템 삭제
            if(cartItems != null && !cartItems.isEmpty()){
                cartItemRepository.deleteAll(cartItems);
            }

            //연관된 메뉴 옵션 아이템 삭제
            if(menuOption.getOptionItems() != null && !menuOption.getOptionItems().isEmpty()){
                optionItemRepository.deleteAll(menuOption.getOptionItems());
            }

            //옵션 삭제
            menuOptionRepository.delete(menuOption);

        }
        else {
            throw new CustomException(ErrorCode.OPTION_NOT_FOUND);
        }
    }
}
