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

    @ManyToOne
    @JoinColumn(name="cart_pk")
    private CartEntity cart;

    @Column
    private String optionItemList;

    @Column
    private Integer totalExtraPrice; //옵션으로 인해 추가된 금액

    @Column
    private Integer menuCount;

    @Column
    private Integer totalPrice; //(메뉴금액+옵션추가금액)X수량
}
