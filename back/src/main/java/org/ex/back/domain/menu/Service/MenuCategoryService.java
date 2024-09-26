package org.ex.back.domain.menu.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ex.back.domain.menu.DTO.MenuCategoryRequestDTO;
import org.ex.back.domain.menu.DTO.MenuCategoryResponseDTO;
import org.ex.back.domain.menu.Repository.MenuCategoryRepository;
import org.ex.back.domain.menu.model.MenuCategoryEntity;
import org.ex.back.domain.store.repository.StoreRepository;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@Service
public class MenuCategoryService {
    //리포지토리 연결
    private final MenuCategoryRepository menuCategoryRepository;
    private final StoreRepository storeRepository;

    //카테고리 생성 로직(스토어 아이디, 생성할 카테고리 이름) , 수정해야함
    public MenuCategoryResponseDTO createdCategory(int id, MenuCategoryRequestDTO request) {
        Optional<StoreEntity> store = storeRepository.findById(id);
        if (store.isPresent()) {
            MenuCategoryEntity menuCategoryEntity = new MenuCategoryEntity();
            MenuCategoryResponseDTO response = new MenuCategoryResponseDTO();

            //카테고리 엔티티 정보 변경
            menuCategoryEntity = menuCategoryEntity.builder()
                    .store(store.get())
                    .subject(request.getSubject())
                    .build();

            //변경된 엔티티 정보를 리포지토리에 저장
            MenuCategoryEntity menuCategorySave = menuCategoryRepository.save(menuCategoryEntity);

            //리스폰스 필드값 변경
            response.setCategory_pk(menuCategorySave.getMenu_category_pk());
            response.setSubject(menuCategorySave.getSubject());

            return response;
        }
        else
        {
            throw new CustomException(ErrorCode.StoreNotFoundException);
        }
    }

    //카테고리 조회 로직(storeId)
    public  List<String> getCategory(int id){
        //스토어 아이디로 가게 조회
        Optional<StoreEntity> store = storeRepository.findById(id);
        if(store.isPresent()){
            //스토어 아이디값과 일치하는 카테고리 리스트에 담기
           List<MenuCategoryEntity> menuCategoryList = menuCategoryRepository.findByStore(store.get());

           //리스트 안에서 subject만 꺼내서 리스트에 담기(stream api사용)
            List<String> categoryList = menuCategoryList.stream()
                    .map(MenuCategoryEntity::getSubject)
                    .collect(Collectors.toList());

        return categoryList;
       }
        else
        {
            throw new CustomException(ErrorCode.StoreNotFoundException);
        }
    }

    //카테고리 수정 로직(수정할 카테고리 아이디, 수정할 이름)
    public MenuCategoryResponseDTO putCategory(int ctid, MenuCategoryRequestDTO request) {

            Optional<MenuCategoryEntity> menuCategoryEntity = menuCategoryRepository.findById(ctid);

            //subject 수정 후 리포지 터리 저장
            if (menuCategoryEntity.isPresent()) {
                MenuCategoryEntity menuCategory = menuCategoryEntity.get();
                menuCategory.setSubject(request.getSubject());
                menuCategoryRepository.save(menuCategory);

                //수정된 repository 값으로 reponse 필드 수정
                MenuCategoryResponseDTO response = new MenuCategoryResponseDTO();
                response.setCategory_pk(menuCategory.getMenu_category_pk());
                response.setSubject(menuCategory.getSubject());

                return response;
            }
            else{
                throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        }


    //카테고리 삭제 로직 (가게 내에서 이루어지기 때문에 스토어 아이티 필요 X)
    public void deleteCategory(int ctid) {
            Optional<MenuCategoryEntity> menuCategoryEntity = menuCategoryRepository.findById(ctid);

            if (menuCategoryEntity.isPresent()) {
                MenuCategoryEntity menuCategory = menuCategoryEntity.get();
                menuCategoryRepository.delete(menuCategory);
        }
            else {
                throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
            }
    }
}
