package org.ex.back.domain.store.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.owner.model.OwnerEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="store_entity")
public class StoreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer store_pk;
	
	@OneToOne
	@JoinColumn(name="owner_pk")
	private OwnerEntity owner;

	@OneToMany
	@JoinColumn(name="store_image_pk")
	private List<StoreImageEntity> storeImages;
	
	@Column
	private String storeName;
	
	@Column
	private String address;
	
	@Column
	private String phone;
	
	@Column(length = 1000)
	private String operatingHours; //영업시간(브레이크타임)

	@Column
	private String storeState; //영업, 휴업, 폐업

	@Column
	private Boolean isOpen; //영업중, 영업종료
	
	@Column
	private Double lat; //위도
	
	@Column
	private Double lng; //경도

	@Column
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column
	private LocalDateTime deletedAt;
}
