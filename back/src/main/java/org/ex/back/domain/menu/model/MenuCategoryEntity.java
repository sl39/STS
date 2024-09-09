package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.store.model.StoreEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="menu_category_entity")
public class MenuCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menu_category_pk;
	
	@ManyToOne
	@JoinColumn(name="store_pk")
	private StoreEntity store;
	
	@Column
	private String subject;
}
