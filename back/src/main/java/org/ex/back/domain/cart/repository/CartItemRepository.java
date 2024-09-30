package org.ex.back.domain.cart.repository;

import org.ex.back.domain.cart.model.CartItemEntity;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {

    //카트 아이쳄 리포지토리에서 해당 메뉴의 아이쳄을 찾기 위한 추상 메소드
    List<CartItemEntity> findByMenu(MenuEntity menu);
 }
