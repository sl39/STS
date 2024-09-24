package org.ex.back.domain.menu.Repository;

import org.ex.back.domain.menu.model.OptionItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionItemRepository extends JpaRepository<OptionItemEntity, Integer> {
}
