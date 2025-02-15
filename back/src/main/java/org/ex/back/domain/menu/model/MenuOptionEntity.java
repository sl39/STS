package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="menu_option_entity")
public class MenuOptionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menu_option_pk;

	@ManyToOne
	@JoinColumn(name = "menu_pk")
	private MenuEntity menu;

	@OneToMany(mappedBy = "menuOption", cascade = CascadeType.ALL)
	private List<OptionItemEntity> optionItems;

	@Column
	private String subject;

	@Column
	private Integer minCount; //최소 선택 개수

	@Column
	private Integer maxCount; //최대 선택 개수

	//옵션 DTO -> 옵션 엔티티 변환을 위한 생성자(stream)
    public MenuOptionEntity(Integer id, String opSubject, Integer maxCount, Integer minCount, List<OptionItemEntity> optionItems) {
    	this.menu_option_pk = id;
		this.subject = opSubject;
		this.maxCount = maxCount;
		this.minCount = minCount;
		this.optionItems = optionItems;
	}
}
