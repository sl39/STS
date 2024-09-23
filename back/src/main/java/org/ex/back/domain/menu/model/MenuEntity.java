package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.store.model.StoreEntity;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="menu_entity")
public class MenuEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menu_pk;
	
	@ManyToOne
	@JoinColumn(name="store_pk")
	private StoreEntity store;
	
	@ManyToOne
	@JoinColumn(name="menu_category_pk")

	private MenuCategoryEntity menuCategory;

	@OneToMany(cascade = CascadeType.ALL)
	private List<MenuOptionEntity> menuOptions;
	
	@Column
	private String name;
	
	@Column
	private Integer price; 
	
	@Column
	private String description;
	
	@Column
	private String imageUrl;
	
	@Column
	private Boolean isBestMenu = false;

	@Column
	private Boolean isAlcohol;
}
