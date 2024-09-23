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

	@OneToMany(cascade = CascadeType.ALL)
	private List<OptionItemEntity> optionItems;
	
	@Column
	private String subject;

	@Column
	private Integer minCount; //최소 선택 개수

	@Column
	private Integer maxCount; //최대 선택 개수
}
