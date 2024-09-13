package org.ex.back.domain.menu.Repository;

import org.ex.back.domain.menu.model.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
}
