package org.ex.back.domain.menu.Repository;

import org.ex.back.domain.menu.model.MenuCategoryEntity;
import org.ex.back.domain.store.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuCategoryRepository extends JpaRepository<MenuCategoryEntity, Integer> {
    //스토어에 따른 카테고리 리스트 불러오기
    List<MenuCategoryEntity> findByStore(StoreEntity store_pk);
}
