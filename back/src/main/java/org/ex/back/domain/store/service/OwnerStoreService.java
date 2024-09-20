package org.ex.back.domain.store.service;

import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.dto.StoreUpdateDTO;
import org.ex.back.domain.store.dto.ImageUrlsDTO; // DTO 추가
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerStoreService {

    private static final Logger logger = LoggerFactory.getLogger(OwnerStoreService.class);

    @Autowired
    private StoreImageRepository storeImageRepository; 
    
    @Autowired
    private OwnerStoreRepository storeRepository;

    @Autowired
    private StoreCategoryConnectorRepository categoryConnectorRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    // 매장 ID로 매장을 찾는 메소드
    public StoreDTO findStoreById(Integer storePk) {
        StoreEntity storeEntity = storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));
        
        return convertToDTO(storeEntity);
    }

    public StoreDTO saveStore(StoreDTO storeDTO) {
        StoreEntity storeEntity = convertToEntity(storeDTO);
        storeEntity.setCreatedAt(LocalDateTime.now());

        // 매장 저장
        StoreEntity savedStore = storeRepository.save(storeEntity);

        // 이미지 저장을 위한 DTO 생성
        ImageUrlsDTO imageUrlsDTO = new ImageUrlsDTO(savedStore.getStore_pk(), null, storeDTO.getStoreImages());
        saveStoreImages(imageUrlsDTO);

        return convertToDTO(savedStore);
    }
    public void saveStoreImages(ImageUrlsDTO imageUrlsDTO) {
        Integer storePk = imageUrlsDTO.getStorePk();
        List<String> storeImages = imageUrlsDTO.getStoreImages();

        // 매장 객체를 가져옴
        StoreEntity storeEntity = storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));

        List<StoreImageEntity> imagesToSave = new ArrayList<>();

        for (String imageUrl : storeImages) {
            StoreImageEntity storeImage = new StoreImageEntity();
            storeImage.setImageUrl(imageUrl);
            imagesToSave.add(storeImage);
        }

        // 이미지를 저장
        storeImageRepository.saveAll(imagesToSave);
    }

    // 매장 정보를 업데이트하는 메소드
    public StoreDTO updateStore(Integer storePk, StoreUpdateDTO storeUpdateDTO) {
        StoreEntity storeEntity = storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));

        // 업데이트된 정보 설정
        storeEntity.setStoreName(storeUpdateDTO.getStoreName());
        storeEntity.setAddress(storeUpdateDTO.getAddress());
        storeEntity.setPhone(storeUpdateDTO.getPhone());
        storeEntity.setOperatingHours(storeUpdateDTO.getOperatingHours());
        storeEntity.setStoreState(storeUpdateDTO.getStoreState());
        storeEntity.setIsOpen(storeUpdateDTO.getIsOpen());
        storeEntity.setLat(storeUpdateDTO.getLat());
        storeEntity.setLng(storeUpdateDTO.getLng());

        // 카테고리 업데이트
        updateStoreCategories(storePk, storeUpdateDTO.getCategoryPks());

        // 매장 저장
        StoreEntity updatedStore = storeRepository.save(storeEntity);
        return convertToDTO(updatedStore);
    }

    // Entity를 DTO로 변환하는 메소드
    private StoreDTO convertToDTO(StoreEntity storeEntity) {
        StoreDTO storeDTO = new StoreDTO();
        /*List<String> list = new ArrayList<>();
        
        for (StoreImageEntity entity : storeEntity.getStoreImages()) {
            String imageUrl = entity.getImageUrl();
            list.add(imageUrl); 
        }

        storeDTO.setStoreImages(list);*/
        storeDTO.setStorePk(storeEntity.getStore_pk());
        storeDTO.setStoreName(storeEntity.getStoreName());
        storeDTO.setAddress(storeEntity.getAddress());
        storeDTO.setPhone(storeEntity.getPhone());
        storeDTO.setOperatingHours(storeEntity.getOperatingHours());
        storeDTO.setStoreState(storeEntity.getStoreState());
        storeDTO.setIsOpen(storeEntity.getIsOpen());
        storeDTO.setLat(storeEntity.getLat());
        storeDTO.setLng(storeEntity.getLng());
        storeDTO.setCreatedAt(storeEntity.getCreatedAt());
        storeDTO.setDeletedAt(storeEntity.getDeletedAt());
     // 이미지 URL 리스트 변환
        List<String> imageUrls = storeEntity.getStoreImages().stream()
                .map(StoreImageEntity::getImageUrl)
                .toList();
        storeDTO.setStoreImages(imageUrls);
        
        return storeDTO;
    }

    // DTO를 Entity로 변환하는 메소드
    private StoreEntity convertToEntity(StoreDTO storeDTO) {
        StoreEntity storeEntity = new StoreEntity();
        List<StoreImageEntity> list = new ArrayList<>();
        for(String imageUrl : storeDTO.getStoreImages()) {
            StoreImageEntity entity = StoreImageEntity.builder().imageUrl(imageUrl).build();
            list.add(entity); 
        }
        
        storeEntity.setStoreImages(list);
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

    // 카테고리 업데이트 메소드
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

    // 카테고리 ID로 카테고리 찾는 메소드
    public StoreCategoryEntity findCategoryById(Integer categoryPk) {
        return storeCategoryRepository.findById(categoryPk)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다. ID: " + categoryPk));
    }

    // 매장 ID로 연결된 카테고리 ID 리스트 가져오기
    public List<Integer> findCategoryPksByStoreId(Integer storePk) {
        return categoryConnectorRepository.findCategoryPksByStoreId(storePk);
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

    // 매장에서 카테고리를 제거하는 메소드
    public void removeCategoryFromStore(Integer storePk, Integer categoryPk) {
        Optional<StoreCategoryConnectorEntity> connector = categoryConnectorRepository.findByStoreIdAndCategoryId(storePk, categoryPk);
        connector.ifPresent(categoryConnectorRepository::delete);
        
        logger.info("매장 ID: {}에서 카테고리 ID: {}가 제거되었습니다.", storePk, categoryPk);
    }
}
