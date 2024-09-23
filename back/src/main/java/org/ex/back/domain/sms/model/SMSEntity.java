package org.ex.back.domain.sms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sms_entity")
public class SMSEntity {

    @Id
    private String phoneNum;

    @Column
    private String certificationCode;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private Boolean certifiedState;
}
