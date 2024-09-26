package org.ex.back.domain.cart.Repository;

import org.ex.back.domain.cart.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {
}
