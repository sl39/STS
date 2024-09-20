package org.ex.back.domain.store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="store_image_entity")
public class StoreImageEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer store_image_pk;
	
	@Column(length = 1000)
	private String imageUrl;
}
