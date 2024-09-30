package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.ex.back.domain.store.model.StoreEntity;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
	private List<MenuOptionEntity> menuOptions;

	//메뉴 생성
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

	@Column
	private LocalDateTime deletedAt;

	//로그 찍기용 toString 메소드
//	@Override
//	public String toString() {
//		return "MenuEntity{" +
//				"menu_pk=" + menu_pk +
//				", name='" + name + '\'' +
//				", description='" + description + '\'' +
//				", price=" + price +
//				", imageUrl='" + imageUrl + '\'' +
//				", isBestMenu=" + isBestMenu +
//				", isAlcohol=" + isAlcohol +
//				", menuCategory=" + (menuCategory != null ? menuCategory.getSubject() : "null") +
//				", menuOptions=" + menuOptions +
//				'}';
//	}
}
