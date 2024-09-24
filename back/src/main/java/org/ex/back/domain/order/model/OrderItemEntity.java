package org.ex.back.domain.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ex.back.domain.menu.model.MenuEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "order_item_entity")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String order_item_pk;

    @ManyToOne
    @JoinColumn(name ="menu_pk")
    private MenuEntity menu;

    @Column
    private Integer menuCount;

    @Column
    private Integer totalPrice;
}
