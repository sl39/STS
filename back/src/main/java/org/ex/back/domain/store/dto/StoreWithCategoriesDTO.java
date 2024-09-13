package org.ex.back.domain.store.dto;

import java.util.List;

import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.model.StoreEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreWithCategoriesDTO {
    private StoreEntity store;
    private List<StoreCategoryEntity> categories;

    public StoreWithCategoriesDTO(StoreEntity store, List<StoreCategoryEntity> categories) {
        this.store = store;
        this.categories = categories;
    }

    
    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public List<StoreCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<StoreCategoryEntity> categories) {
        this.categories = categories;
    }
}
