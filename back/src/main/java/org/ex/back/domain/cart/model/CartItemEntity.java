package org.ex.back.domain.cart.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ex.back.domain.menu.model.MenuEntity;

@NoArgsConstructor
@Getter
@Setter
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

    //옵션으로 인해 추가된 금액 (프론트에서 줘야함!!)
    @Column
    private Integer totalExtraPrice;

    @Column
    private Integer menuCount;

    @Column
    private Integer totalPrice; //(메뉴금액+옵션추가금액)X수량

}
