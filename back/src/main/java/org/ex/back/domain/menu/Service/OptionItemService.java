package org.ex.back.domain.menu.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.menu.DTO.OptionItemDTO;
import org.ex.back.domain.menu.Repository.MenuOptionRepository;
import org.ex.back.domain.menu.Repository.MenuRepository;
import org.ex.back.domain.menu.Repository.OptionItemRepository;
import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.ex.back.domain.menu.model.OptionItemEntity;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Service
@Slf4j
public class OptionItemService {
    private final OptionItemRepository optionItemRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;

    //옵션 아이템 생성 (해당 옵션 id, 생성할 내용)
    public OptionItemDTO createdOptionItem (Integer optionId, OptionItemDTO request){

        //옵션 아이템을 생성할 옵션 아이템 pk로 옵션 값 가지고오기
        Optional<MenuOptionEntity> optionEntity = menuOptionRepository.findById(optionId);

        if(optionEntity.isPresent()){
            MenuOptionEntity option = optionEntity.get();

            //옵션 아이템 엔티티 내용을 넣은뒤 추가
            OptionItemEntity optionItemEntity = new OptionItemEntity();
            optionItemEntity.setName(request.getName());
            optionItemEntity.setExtraPrice(request.getExtraPrice());

            optionItemRepository.save(optionItemEntity);

            //만들어진것을 메뉴옵션 리스트에 추가
            option.getOptionItems().add(optionItemEntity);

            menuOptionRepository.save(option);

            //반환
            OptionItemDTO response = new OptionItemDTO();
            response.setOption_item_pk(optionItemEntity.getOption_item_pk());
            response.setName(optionItemEntity.getName());
            response.setExtraPrice(optionItemEntity.getExtraPrice());

            return response;

        }
        else{
            //해당 옵션이 존재하지 않을 경우 오류 발생
            throw new CustomException(ErrorCode.OPTION_NOT_FOUND);
        }
    }

    //옵션 아이템 수정 (옵션 아이템 아이디, 수정할 옵션 아이디 값)
    public OptionItemDTO updateOptionItem(Integer optionItemPk, OptionItemDTO request) {
        //리포지터리에서 일치하는 아이템 pk 가지고 오기
        Optional<OptionItemEntity> optionItemEntity = optionItemRepository.findById(optionItemPk);
        if(optionItemEntity.isPresent()){
            OptionItemEntity optionItem = optionItemEntity.get();
            optionItem.setOption_item_pk(optionItem.getOption_item_pk());
            optionItem.setName(request.getName());
            optionItem.setExtraPrice(request.getExtraPrice());
            optionItemRepository.save(optionItem);

            OptionItemDTO response = new OptionItemDTO();
            response.setOption_item_pk(optionItem.getOption_item_pk());
            response.setName(request.getName());
            response.setExtraPrice(request.getExtraPrice());

            return response;
        }
        else{
            throw new CustomException(ErrorCode.OPTION_NOT_FOUND);
        }
    }

    //옵션 아이템 삭제
    public void deleteOptionItem(Integer optionItemPk) {
        Optional<OptionItemEntity> optionItemEntity = optionItemRepository.findById(optionItemPk);
        if(optionItemEntity.isPresent()){
            OptionItemEntity optionItem = optionItemEntity.get();
            optionItemRepository.delete(optionItem);
        }
        else{
            throw new CustomException(ErrorCode.OPTION_ITEM_NOT_FOUND);
        }
    }


}
