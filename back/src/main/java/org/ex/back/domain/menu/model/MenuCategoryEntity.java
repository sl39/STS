package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.store.model.StoreEntity;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="menu_category_entity")
public class MenuCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menu_category_pk;

	@OneToMany(mappedBy = "menuCategory", cascade = CascadeType.ALL)
	private List<MenuEntity> menuList;
	
	@ManyToOne
	@JoinColumn(name="store_pk")
	private StoreEntity store;
	
	@Column
	private String subject;

}
