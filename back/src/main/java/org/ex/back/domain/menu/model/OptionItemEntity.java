package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="option_item_entity")
public class OptionItemEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer option_item_pk;

	@ManyToOne
	@JoinColumn(name = "menu_option_pk")
	private MenuOptionEntity menuOption;

	@Column
	private String name;

	@Column
	private Integer extraPrice;

	//옵션 아이템 DTO -> 옵션 아이템 엔티티 변환을 위한 생성자 (stream API)
	public OptionItemEntity(Integer id, String name, Integer extraPrice) {
		this.option_item_pk= id;
		this.name = name;
		this.extraPrice = extraPrice;
	}

}
