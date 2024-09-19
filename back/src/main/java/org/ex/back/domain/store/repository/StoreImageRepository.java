package org.ex.back.domain.store.repository;

import org.ex.back.domain.store.model.StoreImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreImageRepository extends JpaRepository<StoreImageEntity, Integer> {

    @Query(value = "SELECT i.image_url " +
                   "FROM store_image i " +
                   "WHERE i.store_pk = :storeId", // store_entity_id를 store_pk로 수정
           nativeQuery = true)
    List<StoreImageEntity> findAllByStoreId(@Param("storeId") Integer storeId);
}
