//package org.ex.back.domain.menu.Service;
//
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.ex.back.domain.menu.DTO.MenuRequestDTO;
//import org.ex.back.domain.menu.DTO.MenuResponseDTO;
//import org.ex.back.domain.menu.Repository.MenuCategoryRepository;
//import org.ex.back.domain.menu.Repository.MenuRepository;
//import org.ex.back.domain.menu.model.MenuCategoryEntity;
//import org.ex.back.domain.menu.model.MenuEntity;
//import org.ex.back.domain.store.Repository.StoreRepository;
//import org.ex.back.domain.store.model.StoreEntity;
//import org.springframework.stereotype.Service;
//
//import java.awt.*;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Data
//@Service
//@Slf4j
//public class MenuService {
//    private final MenuRepository menuRepository;
//    private final StoreRepository storeRepository;
//    private final MenuCategoryService menuCategoryService;
//    private final MenuCategoryRepository menuCategoryRepository;
//
//    //메뉴 생성 (storeId)
//    public MenuResponseDTO createMenu(int StoreId, MenuRequestDTO request) {
//        Optional<StoreEntity> store = storeRepository.findById(StoreId);
//
//        if(store.isPresent()) {
//            StoreEntity storeEntity = store.get();
//
//            //가게에 존재하는 카테고리 정보를 받아 현재 메뉴의 카테고리로 바꾸기 위함
//            Optional<MenuCategoryEntity> menuCategory = menuCategoryRepository.findById(request.getCategory_pk());
//
//            if(menuCategory.isPresent()) {
//                //request의 카테고리pk와 일치하는 카테고리 객체 생성
//                MenuCategoryEntity categoryEntity = menuCategory.get();
//
//                //메뉴 엔티티의 내용을 request 값으로 변경후 저장
//                MenuEntity menu = MenuEntity.builder()
//                        .
//            }
//        }
//
//    }
//
//}
