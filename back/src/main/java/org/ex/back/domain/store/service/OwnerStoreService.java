package org.ex.back.domain.store.service;

import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.repository.OwnerStoreRepository;
import org.ex.back.domain.store.repository.StoreCategoryConnectorRepository;
import org.ex.back.domain.store.repository.StoreCategoryRepository; // 추가된 Repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OwnerStoreService {

    private static final Logger logger = LoggerFactory.getLogger(OwnerStoreService.class);

    @Autowired
    private OwnerStoreRepository storeRepository;

    @Autowired
    private StoreCategoryConnectorRepository categoryConnectorRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository; // 카테고리를 찾기 위한 Repository 추가

    // 매장 ID로 매장을 찾는 메소드
    public StoreEntity findStoreById(Integer storePk) {
        return storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));
    }

    // 매장을 저장하는 메소드
    public StoreEntity saveStore(StoreEntity store) {
        return storeRepository.save(store);
    }

    // 매장 영업 상태 전환 메소드
    public StoreEntity toggleStoreOpenStatus(Integer storePk) {
        StoreEntity store = findStoreById(storePk);
        store.setIsOpen(!store.getIsOpen());
        return saveStore(store);
    }

    // 카테고리 ID로 카테고리 찾는 메소드
    public StoreCategoryEntity findCategoryById(Integer categoryPk) {
        return storeCategoryRepository.findById(categoryPk) // 수정된 부분
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다. ID: " + categoryPk));
    }

    // 매장에 카테고리를 추가하는 메소드
    public void addCategoryToStore(Integer storePk, Integer categoryPk) {
        StoreEntity store = findStoreById(storePk);
        StoreCategoryEntity storeCategory = findCategoryById(categoryPk);
        
        StoreCategoryConnectorEntity connector = new StoreCategoryConnectorEntity();
        connector.setStore(store); // StoreEntity 설정
        connector.setStoreCategory(storeCategory); // StoreCategoryEntity 설정
        categoryConnectorRepository.save(connector);
        
        logger.info("매장 ID: {}에 카테고리 ID: {}가 추가되었습니다.", storePk, categoryPk);
    }


}
