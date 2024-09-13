package org.ex.back.domain.store.repository;

import java.util.List;

import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategoryEntity,Integer> {
}
