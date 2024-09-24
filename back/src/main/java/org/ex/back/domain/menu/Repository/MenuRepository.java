package org.ex.back.domain.menu.Repository;

import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.store.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
    //StoreId와 일치하는 메뉴엔티티 타입 리수투 불러오는 추상 메소드 정의
    List<MenuEntity> findByStore(StoreEntity store_pk);
    List<MenuEntity> findByNameContaining(String name);
}