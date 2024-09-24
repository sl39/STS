package org.ex.back.domain.waiting.repository;

import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.waiting.model.WaitingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitingRepository extends JpaRepository<WaitingEntity, Integer> {

    List<WaitingEntity> findAllByStore(StoreEntity store);
}
