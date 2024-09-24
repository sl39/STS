package org.ex.back.domain.waiting.repository;

import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.waiting.model.WaitingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<WaitingEntity, Integer> {

    List<WaitingEntity> findAllByStore(StoreEntity store);

    // 웨이팅 리스트 오름차순으로 정렬해서 반환
    @Query(value = "SELECT w FROM WaitingEntity w " +
            "WHERE w.store.store_pk = :storePk AND w.waitingState = 'STANDBY' " +
            "ORDER BY w.orderQueue ASC")
    List<WaitingEntity> findStandbyTeamList(@Param("storePk") Integer storePk);

    // orderQueue 최대값 조회 (null인 경우에 0 반환)
    @Query("SELECT COALESCE(MAX(w.orderQueue), 0) FROM WaitingEntity w " +
            "WHERE w.store.store_pk = :storePk")
    Integer findMaxOrderQueueByStore(@Param("storePk") Integer storePk);

}
