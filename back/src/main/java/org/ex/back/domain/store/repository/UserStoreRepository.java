package org.ex.back.domain.store.repository;

import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoreRepository extends JpaRepository<StoreEntity, Integer> {

    // 매장 이름에 특정 문자열이 포함된 매장을 검색하는 메소드
    List<StoreEntity> findByStoreNameContaining(String storeName);

    // 특정 카테고리(subject)를 기준으로 매장을 검색하는 메소드
    @Query(value = "SELECT s.* " +
                   "FROM store_entity s " +
                   "INNER JOIN store_category_connector_entity sc ON s.store_pk = sc.store_pk " +
                   "WHERE sc.store_category_pk = :subject ",
           nativeQuery = true)
    List<StoreEntity> findStoresByCategory(@Param("subject") Integer storeCategoryPk);

    // 특정 store_pk에 해당하는 category_pk를 검색하는 메소드
    @Query(value = "SELECT sc.store_category_pk " +
                   "FROM store_category_connector_entity sc " +
                   "WHERE sc.store_pk = :storePk",
           nativeQuery = true)
    List<Integer> findCategoryByStores(@Param("storePk") Integer storePk);
}
