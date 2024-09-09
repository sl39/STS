package org.ex.back.domain.order.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.store.model.StoreEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "order_entity")
public class OrderEntity {

	@Id
	private String order_pk; //랜덤 아이디 생성
	
	@ManyToOne
	@JoinColumn(name ="user_pk")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name ="store_pk")
	private StoreEntity store;

	@OneToMany
	@JoinColumn(name ="order_item_pk")
	private List<OrderItemEntity> OrderItems;

	@Column
	private String tableNumber;
	
	@Column
	private Integer totalPrice;

	@Column
	private String guestPhone;

	@Column
	private String paymentType;
	
	@Column
	private Integer paidPrice;

	@Column
	private Boolean isPaidAll = false;

	@Column
	private Boolean isClear = false;
	
	@Column
	private LocalDateTime orderedAt = LocalDateTime.now();
}
