package org.ex.back.domain.cart.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.menu.model.MenuEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

	@ManyToOne
	@JoinColumn(name="store_pk")
	private StoreEntity store;

	@Column
	private String tableNumber;

	@Column
	private Integer totalPrice;
}
