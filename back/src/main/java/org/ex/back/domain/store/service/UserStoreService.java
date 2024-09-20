package org.ex.back.domain.store.service;

import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.repository.StoreCategoryConnectorRepository;
import org.ex.back.domain.store.repository.UserStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStoreService {

    @Autowired
    private UserStoreRepository storeRepository;

    @Autowired
    private StoreCategoryConnectorRepository storeCategoryConnectorRepository;

    // 모든 매장을 조회하는 메소드
    public List<StoreEntity> findAllStores() {
        return storeRepository.findAll();
    }

    // 매장 이름에 특정 문자열이 포함된 매장을 검색하는 메소드
    public List<StoreEntity> searchStoresByName(String name) {
        return storeRepository.findByStoreNameContaining(name);
    }

    // 매장 ID로 매장을 찾는 메소드
    public StoreEntity findStoreById(Integer storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storeId));
    }

    // 주어진 connectorPk로 StoreCategoryConnectorEntity를 찾는 메소드
    public StoreCategoryConnectorEntity findStoreCategoryConnectorById(Integer connectorPk) {
        return storeCategoryConnectorRepository.findById(connectorPk)
                .orElseThrow(() -> new RuntimeException("통합 정보를 찾을 수 없습니다. ID: " + connectorPk));
    }

    // 특정 카테고리에 해당하는 매장을 조회하는 메소드
    public List<StoreEntity> findStoresByCategory(Integer storeCategoryPk) {
        return storeRepository.findStoresByCategory(storeCategoryPk);
    }

    // 특정 매장에 포함된 카테고리 조회 메소드
    public List<Integer> findCategoriesByStoreId(Integer storeId) {
        return storeRepository.findCategoryByStores(storeId);
    }

}

