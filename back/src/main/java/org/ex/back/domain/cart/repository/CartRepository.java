package org.ex.back.domain.cart.repository;

import org.ex.back.domain.cart.model.CartEntity;
import org.ex.back.domain.cart.model.CartItemEntity;
import org.ex.back.domain.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    Optional<CartEntity> findByUser(UserEntity user);

    //카트 와 관련된 아이쳄을 삭제하는 메소드
    @Query("SELECT c FROM CartEntity c JOIN c.cartItems ci WHERE ci = :cartItem")
    CartEntity findByCartItems(@Param("cartItem") CartItemEntity cartItem);

}
