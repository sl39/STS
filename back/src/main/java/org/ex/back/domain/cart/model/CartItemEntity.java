package org.ex.back.domain.cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.user.model.UserEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="cart_item_entity")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cart_item_pk;

    @ManyToOne
    @JoinColumn(name="menu_pk")
    private MenuEntity menu;

    @Column
    private Integer menuCount;

    @Column
    private Integer totalPrice;
}
