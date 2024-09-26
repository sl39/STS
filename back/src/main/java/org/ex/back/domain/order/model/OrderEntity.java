package org.ex.back.domain.order.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.store.model.StoreEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "order_entity")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String order_pk; //랜덤 아이디 생성
	
	@ManyToOne
	@JoinColumn(name ="user_pk")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name ="store_pk")
	@JsonBackReference
	private StoreEntity store;

	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderItemEntity> OrderItems;

	@Column
	private String payNumber; //거래 번호

	@Column
	private String tableNumber; //테이블 번호
	
	@Column
	private Integer totalPrice;	//총금액

	@Column
	private String guestPhone; //휴대폰번호

	@Column
	private String paymentType; //지불 방식
	
	@Column
	private Integer paidPrice; //결제된 금액

	@Column
	private Boolean isPaidAll = false; //결제완료 여부

	@Column
	private Boolean isClear = false; //음식 완료
	
	@Column
	private LocalDateTime orderedAt = LocalDateTime.now(); //시간
}
