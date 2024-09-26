package org.ex.back.domain.cart.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.user.model.UserEntity;

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

	@OneToMany(cascade = CascadeType.ALL)
	private List<CartItemEntity> cartItems;

	@ManyToOne
	@JoinColumn(name="store_pk")
	private StoreEntity store;

	@Column
	private String tableNumber;

	@Column
	private Integer totalPrice; // 카트 총금액
}
