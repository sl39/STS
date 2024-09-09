package org.ex.back.domain.cart.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.menu.model.MenuEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="cart_entity")
public class CartEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cart_pk;

	@OneToOne
	@JoinColumn(name="user_pk")
	private UserEntity user;

	@OneToMany
	@JoinColumn(name="cart_item_pk")
	private List<CartItemEntity> cartItems;

	@Column
	private String tableNumber;

	@Column
	private String guestPhone;

	@Column
	private Integer totalPrice;
}
