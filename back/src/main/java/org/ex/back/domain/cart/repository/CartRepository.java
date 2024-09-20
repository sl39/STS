package org.ex.back.domain.cart.repository;

import org.ex.back.domain.cart.model.CartEntity;
import org.ex.back.domain.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    Optional<CartEntity> findByUser(UserEntity user);
}
