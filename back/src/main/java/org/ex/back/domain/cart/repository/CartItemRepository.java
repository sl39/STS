package org.ex.back.domain.cart.repository;

import org.ex.back.domain.cart.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {
}
