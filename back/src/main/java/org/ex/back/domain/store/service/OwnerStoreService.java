package org.ex.back.domain.store.service;

import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.repository.OwnerStoreRepository;
import org.ex.back.domain.store.repository.StoreCategoryConnectorRepository;
import org.ex.back.domain.store.repository.StoreCategoryRepository;
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
    private StoreCategoryRepository storeCategoryRepository;

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
        return storeCategoryRepository.findById(categoryPk)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다. ID: " + categoryPk));
    }

    // 매장에 카테고리를 추가하는 메소드
    public void addCategoryToStore(Integer storePk, Integer categoryPk) {
        StoreEntity store = findStoreById(storePk);
        StoreCategoryEntity storeCategory = findCategoryById(categoryPk);
        
        StoreCategoryConnectorEntity connector = new StoreCategoryConnectorEntity();
        connector.setStore(store);
        connector.setStoreCategory(storeCategory);
        categoryConnectorRepository.save(connector);
        
        logger.info("매장 ID: {}에 카테고리 ID: {}가 추가되었습니다.", storePk, categoryPk);
    }

    // 매장 ID로 연결된 카테고리 ID 리스트 가져오기
    public List<Integer> findCategoryPksByStoreId(Integer storePk) {
        return categoryConnectorRepository.findCategoryPksByStoreId(storePk);
    }

    // 매장 카테고리 업데이트 메소드
    public void updateStoreCategories(Integer storePk, List<Integer> newCategoryPks) {
        // 기존 카테고리 ID 리스트 가져오기
        List<Integer> existingCategoryPks = findCategoryPksByStoreId(storePk);

        // 기존 카테고리 삭제
        for (Integer existingPk : existingCategoryPks) {
            if (!newCategoryPks.contains(existingPk)) {
                removeCategoryFromStore(storePk, existingPk);
            }
        }

        // 새로운 카테고리 추가
        for (Integer newPk : newCategoryPks) {
            if (!existingCategoryPks.contains(newPk)) {
                addCategoryToStore(storePk, newPk);
            }
        }
    }

    // 매장에서 카테고리를 제거하는 메소드
    public void removeCategoryFromStore(Integer storePk, Integer categoryPk) {
        // 카테고리 커넥터 엔티티를 찾아서 삭제
        Optional<StoreCategoryConnectorEntity> connector = categoryConnectorRepository.findByStoreIdAndCategoryId(storePk, categoryPk);
        connector.ifPresent(categoryConnectorRepository::delete);
        
        logger.info("매장 ID: {}에서 카테고리 ID: {}가 제거되었습니다.", storePk, categoryPk);
    }

}
