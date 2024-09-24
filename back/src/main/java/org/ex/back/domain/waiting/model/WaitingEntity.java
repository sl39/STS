package org.ex.back.domain.waiting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.waiting.dto.WaitingResponseDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="waiting_entity")
public class WaitingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer waiting_pk;

    @ManyToOne
    @JoinColumn(name="store_pk")
    private StoreEntity store;

    @Column
    private String phone;

    @Column
    private Integer headCount;

    @Column
    private Integer orderQueue;

    @Column
    private String waitingState;
}

//public WaitingResponseDto toDto() {
//
//}
