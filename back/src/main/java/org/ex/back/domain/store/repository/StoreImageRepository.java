package org.ex.back.domain.store.repository;

import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.model.StoreImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreImageRepository extends JpaRepository<StoreImageEntity, Integer> {
}

