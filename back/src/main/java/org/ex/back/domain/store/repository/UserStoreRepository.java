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

    // 주어진 위도(lat)와 경도(lng)를 기준으로 1000미터 이내의 매장을 검색하는 메소드
    @Query(value = "SELECT s.* " +
                   "FROM store_entity s " +
                   "WHERE (6371000 * acos(cos(radians(:lat)) * cos(radians(s.lat)) " +
                   "* cos(radians(s.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(s.lat)))) < 1000",
           nativeQuery = true)
    List<StoreEntity> findStoresWithinDistance(@Param("lng") double lng, @Param("lat") double lat);

    // 특정 카테고리(subject)와 이름(storeName)을 기준으로 매장을 검색하는 메소드
    @Query(value = "SELECT s.* " +
                   "FROM store_entity s " +
                   "INNER JOIN store_category_connector_entity sc ON s.store_pk = sc.store_pk " +
                   "WHERE sc.store_category_pk = :subject " +
                   "AND s.storeName LIKE %:storeName%",
           nativeQuery = true)
    List<StoreEntity> findStoresByCategoryAndName(@Param("subject") Integer storeCategoryPk, @Param("storeName") String storeName);

    // 특정 메뉴 이름(menuName)을 포함하는 매장을 검색하는 메소드
    @Query(value = "SELECT s.* " +
                   "FROM store_entity s " +
                   "INNER JOIN menu m ON s.store_pk = m.store_id " +
                   "WHERE m.menu_name LIKE %:menuName%",
           nativeQuery = true)
    List<StoreEntity> findStoresByMenuName(@Param("menuName") String menuName);

    // 특정 카테고리(subject)를 기준으로 매장을 검색하는 메소드
    @Query(value = "SELECT s.* " +
                   "FROM store_entity s " +
                   "INNER JOIN store_category_connector_entity sc ON s.store_pk = sc.store_pk " +
                   "WHERE sc.store_category_pk = :subject ",
           nativeQuery = true)
    List<StoreEntity> findStoresByCategory(@Param("subject") Integer storeCategoryPk);

    // 주어진 위도(lat)와 경도(lng)를 기준으로 1000미터 이내의 매장을 특정 카테고리(subject)와 함께 검색하는 메소드
    @Query(value = "SELECT s.* " +
                   "FROM store_entity s " +
                   "INNER JOIN store_category_connector_entity sc ON s.store_pk = sc.store_pk " +
                   "WHERE (6371000 * acos(cos(radians(:lat)) * cos(radians(s.lat)) " +
                   "* cos(radians(s.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(s.lat)))) < 1000 " +
                   "AND sc.store_category_pk = :subject",
           nativeQuery = true)
    List<StoreEntity> findStoresWithinDistanceByCategory(@Param("lng") double lng, @Param("lat") double lat, @Param("subject") Integer storeCategoryPk);
    
    // 특정 store_pk에 해당하는 category_pk를 검색하는 메소드
    @Query(value = "SELECT sc.store_category_pk " +
                   "FROM store_category_connector_entity sc " +
                   "WHERE sc.store_pk = :storePk",
           nativeQuery = true)
    List<Integer> findCategoryByStores(@Param("storePk") Integer storePk);
}
