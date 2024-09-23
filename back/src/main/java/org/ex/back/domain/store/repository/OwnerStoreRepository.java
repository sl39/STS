package org.ex.back.domain.store.repository;

import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.ex.back.domain.store.model.StoreImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerStoreRepository extends JpaRepository<StoreEntity, Integer> {
    // 특정 store_pk에 해당하는 category_pk를 검색하는 메소드
    @Query(value = "SELECT sc.store_category_pk " +
                   "FROM store_category_connector_entity sc " +
                   "WHERE sc.store_pk = :storePk",
           nativeQuery = true)
    List<Integer> findCategoryPksByStorePk(@Param("storePk") Integer storePk);
}
