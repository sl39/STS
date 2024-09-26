package org.ex.back.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreCategoryConnectorRepository extends JpaRepository<StoreCategoryConnectorEntity, Integer> {

    // 매장 ID에 해당하는 카테고리 ID 리스트를 가져오는 쿼리
    @Query("SELECT scc.storeCategory.store_category_pk FROM StoreCategoryConnectorEntity scc WHERE scc.store.store_pk = :storePk")
    List<Integer> findCategoryPksByStoreId(@Param("storePk") Integer storePk);

    // 주어진 매장 ID와 카테고리 ID에 해당하는 커넥터 엔티티를 찾는 메서드
    @Query("SELECT scc FROM StoreCategoryConnectorEntity scc WHERE scc.store.store_pk = :storePk AND scc.storeCategory.store_category_pk = :categoryPk")
    Optional<StoreCategoryConnectorEntity> findByStoreIdAndCategoryId(@Param("storePk") Integer storePk, @Param("categoryPk") Integer categoryPk);
}
