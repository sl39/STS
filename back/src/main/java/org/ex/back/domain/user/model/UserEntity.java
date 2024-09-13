package org.ex.back.domain.user.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="user_entity")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_pk;
	
	@Column
	private String name;
	
	@Column
	private String email;
	
	@Column
	private String socialId;
	
	@Column
	private String socialType;

	@Column
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column
	private LocalDateTime deletedAt;
}
