package org.ex.back.domain.menu.Repository;

import org.ex.back.domain.menu.model.MenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOptionEntity, Integer> {
}
