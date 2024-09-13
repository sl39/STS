package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="menu_option_entity")
public class MenuOptionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menu_option_pk;
	
	@OneToMany
	@JoinColumn(name="option_item_pk")
	private List<OptionItemEntity> optionItems;
	
	@Column
	private String subject;
	
	@Column
	private Boolean isEssential = true;
	
	@Column
	private Boolean isSelected = false;
}
