package org.ex.back.domain.owner.repository;

import org.ex.back.domain.menu.model.OptionItemEntity;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Integer> {

    Optional<OwnerEntity> findById(String id);
}
