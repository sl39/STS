package org.ex.back.domain.menu.model;

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
