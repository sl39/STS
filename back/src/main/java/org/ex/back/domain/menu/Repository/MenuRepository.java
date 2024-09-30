package org.ex.back.domain.menu.Repository;

import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.ex.back.domain.store.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
    //StoreId와 일치하는 메뉴엔티티 타입 리수투 불러오는 추상 메소드 정의
    List<MenuEntity> findByStore(StoreEntity store_pk);
    List<MenuEntity> findByNameContaining(String name);
 // 메뉴 이름과 정확히 일치하는 항목 검색
    List<MenuEntity> findByName(String name);

    // 리스트 조회
    List<MenuEntity> findAllByDeletedAtIsNullAndStore(StoreEntity store_pk);

    // 단건 조회
    //Optional<MenuEntity> findByMenu_pkAndDeletedAtIsNull(Integer menu_pk);
}