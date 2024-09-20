package org.ex.back.domain.store.service;

import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.repository.StoreCategoryConnectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreCategoryConnectorService {

    @Autowired
    private StoreCategoryConnectorRepository connectorRepository;

    public StoreCategoryConnectorEntity addCategoryToStore(StoreEntity store, StoreCategoryEntity category) {
        StoreCategoryConnectorEntity connector = new StoreCategoryConnectorEntity();
        connector.setStore(store);
        connector.setStoreCategory(category);
        return connectorRepository.save(connector);
    }
}
