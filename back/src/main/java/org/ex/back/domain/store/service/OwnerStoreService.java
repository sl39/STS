package org.ex.back.domain.store.service;

import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreImageEntity;
import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.repository.OwnerStoreRepository;
import org.ex.back.domain.store.repository.StoreCategoryConnectorRepository;
import org.ex.back.domain.store.repository.StoreCategoryRepository;
import org.ex.back.domain.store.repository.StoreImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerStoreService {

    private static final Logger logger = LoggerFactory.getLogger(OwnerStoreService.class);

    @Autowired
    private StoreImageRepository storeImageRepository; // 이미지 저장을 위한 리포지토리
    
    @Autowired
    private OwnerStoreRepository storeRepository;

    @Autowired
    private StoreCategoryConnectorRepository categoryConnectorRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    // 이미지 저장
    public StoreImageEntity saveStoreImage(StoreImageEntity image) {
        // 매장 ID를 별도로 관리하는 로직이 필요할 수 있습니다.
        return storeImageRepository.save(image);
    }
    public List<StoreImageEntity> getStoreImageUrls(Integer storePk) {
        return storeImageRepository.findAllByStoreId(storePk);
    }

    // 매장 ID로 매장을 찾는 메소드
    public StoreDTO findStoreById(Integer storePk) {
        StoreEntity storeEntity = storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));
        
        return convertToDTO(storeEntity);
    }

 // 매장을 저장하는 메소드
    public StoreDTO saveStore(StoreDTO storeDTO) {
        StoreEntity storeEntity = convertToEntity(storeDTO);
        
        // 매장 저장
        StoreEntity savedStore = storeRepository.save(storeEntity);

        // 이미지 URL 저장
        if (storeDTO.getStoreImageUrls() != null) {
            for (String imageUrl : storeDTO.getStoreImageUrls()) {
                StoreImageEntity imageEntity = new StoreImageEntity();
                imageEntity.setImageUrl(imageUrl);
                // 여기서 매장 ID를 관리하는 로직을 추가할 수 있습니다.
                // 예: 매장 ID를 이용해 저장할 수 있는 별도의 메서드 호출
                
                saveStoreImage(imageEntity); // 이미지 저장
            }
        }

        return convertToDTO(savedStore);
    }

    // 매장 영업 상태 전환 메소드
    public StoreDTO toggleStoreOpenStatus(Integer storePk) {
        StoreEntity store = storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));
        store.setIsOpen(!store.getIsOpen());
        return saveStore(convertToDTO(store));
    }

    // 카테고리 ID로 카테고리 찾는 메소드
    public StoreCategoryEntity findCategoryById(Integer categoryPk) {
        return storeCategoryRepository.findById(categoryPk)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다. ID: " + categoryPk));
    }

    // 매장에 카테고리를 추가하는 메소드
    public void addCategoryToStore(Integer storePk, Integer categoryPk) {
        StoreEntity store = storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));
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
        List<Integer> existingCategoryPks = findCategoryPksByStoreId(storePk);

        // 기존 카테고리 제거
        existingCategoryPks.forEach(existingPk -> {
            if (!newCategoryPks.contains(existingPk)) {
                removeCategoryFromStore(storePk, existingPk);
            }
        });

        // 새로운 카테고리 추가
        newCategoryPks.forEach(newPk -> {
            if (!existingCategoryPks.contains(newPk)) {
                addCategoryToStore(storePk, newPk);
            }
        });
    }

    // 매장에서 카테고리를 제거하는 메소드
    public void removeCategoryFromStore(Integer storePk, Integer categoryPk) {
        Optional<StoreCategoryConnectorEntity> connector = categoryConnectorRepository.findByStoreIdAndCategoryId(storePk, categoryPk);
        connector.ifPresent(categoryConnectorRepository::delete);
        
        logger.info("매장 ID: {}에서 카테고리 ID: {}가 제거되었습니다.", storePk, categoryPk);
    }

 // Entity를 DTO로 변환하는 메소드
    private StoreDTO convertToDTO(StoreEntity storeEntity) {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStorePk(storeEntity.getStore_pk());
        storeDTO.setStoreName(storeEntity.getStoreName());
        storeDTO.setAddress(storeEntity.getAddress());
        storeDTO.setPhone(storeEntity.getPhone());
        storeDTO.setOperatingHours(storeEntity.getOperatingHours());
        storeDTO.setStoreState(storeEntity.getStoreState());
        storeDTO.setIsOpen(storeEntity.getIsOpen());
        storeDTO.setLat(storeEntity.getLat());
        storeDTO.setLng(storeEntity.getLng());
        if (storeEntity.getOwner() != null) {
            storeDTO.setOwnerPk(storeEntity.getOwner().getOwner_pk());
        }
        
        return storeDTO;
    }

    // DTO를 Entity로 변환하는 메소드
    private StoreEntity convertToEntity(StoreDTO storeDTO) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStore_pk(storeDTO.getStorePk());
        storeEntity.setStoreName(storeDTO.getStoreName());
        storeEntity.setAddress(storeDTO.getAddress());
        storeEntity.setPhone(storeDTO.getPhone());
        storeEntity.setOperatingHours(storeDTO.getOperatingHours());
        storeEntity.setStoreState(storeDTO.getStoreState());
        storeEntity.setIsOpen(storeDTO.getIsOpen());
        storeEntity.setLat(storeDTO.getLat());
        storeEntity.setLng(storeDTO.getLng());
        storeEntity.setCreatedAt(storeDTO.getCreatedAt());
        storeEntity.setDeletedAt(storeDTO.getDeletedAt());
        return storeEntity;
    }

    // 매장에 속한 이미지 삭제
    public void deleteStoreImagesByStorePk(Integer storePk) {
        List<StoreImageEntity> images = storeImageRepository.findAllByStoreId(storePk);
        for (StoreImageEntity image : images) {
            storeImageRepository.delete(image); // StoreImageEntity 삭제
        }
    }
}