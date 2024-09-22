package org.ex.back.domain.menu.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="option_item_entity")
public class OptionItemEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer option_item_pk;

	@Column
	private String name;

	@Column
	private Integer extraPrice;
}
