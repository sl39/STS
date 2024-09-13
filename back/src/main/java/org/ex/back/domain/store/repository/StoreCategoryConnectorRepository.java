package org.ex.back.domain.store.repository;

import java.util.Optional;

import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryConnectorRepository extends JpaRepository<StoreCategoryConnectorEntity,Integer> {

}
