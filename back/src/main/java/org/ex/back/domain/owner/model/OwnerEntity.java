package org.ex.back.domain.owner.model;

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
@Table(name="owner_entity")
public class OwnerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer owner_pk;
	
	@Column
	private String id;
	
	@Column
	private String password;
	
	@Column
	private String email;
	
	@Column
	private String businessNumber; //사업자등록번호
	
	@Column
	private String name;
	
	@Column
	private String phone;

	@Column
	private String bankName;

	@Column
	private String bankAccount;

	@Column
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column
	private LocalDateTime deletedAt;
}
