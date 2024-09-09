package org.ex.back.domain.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="store_category_connector_entity")
public class StoreCategoryConnectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer connector_pk;

    @ManyToOne
    @JoinColumn(name="store_pk")
    private StoreEntity store;

    @ManyToOne
    @JoinColumn(name="store_category_pk")
    private StoreCategoryEntity storeCategory;
}
