package org.ex.back.domain.store.service;

import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.dto.StoreUpdateDTO;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.ex.back.domain.store.dto.ImageUrlsDTO; // DTO 추가
import org.ex.back.domain.store.dto.ResponseDTO;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreImageEntity;
import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.repository.OwnerStoreRepository;
import org.ex.back.domain.store.repository.StoreCategoryConnectorRepository;
import org.ex.back.domain.store.repository.StoreCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerStoreService {


private static final Logger logger = LoggerFactory.getLogger(OwnerStoreService.class);
@Autowired
private EntityManager entityManager;

@Autowired
private OwnerStoreRepository storeRepository;

@Autowired
private StoreCategoryConnectorRepository categoryConnectorRepository;

@Autowired
private StoreCategoryRepository storeCategoryRepository;

@Autowired
private GeocodingService geocodingService; // GeocodingService 주입

// 매장 ID로 매장을 찾는 메소드
public StoreDTO findStoreById(Integer storePk) {
    StoreEntity storeEntity = storeRepository.findById(storePk)
            .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));
    
    return convertToDTO(storeEntity);
}

public StoreDTO saveStore(StoreDTO storeDTO) {
    // 유효성 검사
    if (storeDTO.getStoreName() == null || storeDTO.getAddress() == null) {
        throw new IllegalArgumentException("매장 이름과 주소는 필수입니다.");
    }

    // 주소로부터 좌표 가져오기
    Double[] coordinates = geocodingService.getCoordinates(storeDTO.getAddress());
    if (coordinates != null) {
        storeDTO.setLat(coordinates[0]); // 위도 설정
        storeDTO.setLng(coordinates[1]); // 경도 설정
        logger.info("저장할 좌표: 위도 = {}, 경도 = {}", storeDTO.getLat(), storeDTO.getLng());
    } else {
        throw new RuntimeException("좌표를 찾지 못했습니다. 주소: " + storeDTO.getAddress());
    }

    // 매장 정보 로그
    logger.info("저장할 매장 정보: {}", storeDTO);

    // 매장 정보 저장
    try {
        StoreEntity savedStore = storeRepository.save(convertToEntity(storeDTO));
        logger.info("저장된 매장 정보: {}", savedStore);
        return convertToDTO(savedStore);
    } catch (Exception e) {
        logger.error("매장 저장 중 오류 발생: {}", e.getMessage(), e);
        throw new RuntimeException("매장 저장 중 오류 발생: " + e.getMessage());
    }
}

public StoreDTO updateStore(Integer storePk, StoreUpdateDTO storeUpdateDTO) {
    // 매장 엔티티를 ID로 조회
    StoreEntity storeEntity = storeRepository.findById(storePk)
            .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storePk));

    // 업데이트된 정보 설정 (null 체크 및 기존 값 유지)
    if (storeUpdateDTO.getStoreName() != null && !storeUpdateDTO.getStoreName().isEmpty()) {
        storeEntity.setStoreName(storeUpdateDTO.getStoreName());
    }

    if (storeUpdateDTO.getPhone() != null && !storeUpdateDTO.getPhone().isEmpty()) {
        storeEntity.setPhone(storeUpdateDTO.getPhone());
    }

    if (storeUpdateDTO.getOperatingHours() != null && !storeUpdateDTO.getOperatingHours().isEmpty()) {
        storeEntity.setOperatingHours(storeUpdateDTO.getOperatingHours());
    }

    if (storeUpdateDTO.getStoreState() != null) {
        storeEntity.setStoreState(storeUpdateDTO.getStoreState());
    }

    // 이미지 업데이트
    if (storeUpdateDTO.getStoreImages() != null) {
        List<StoreImageEntity> images = new ArrayList<>();
        for (String imageUrl : storeUpdateDTO.getStoreImages()) {
            StoreImageEntity storeImage = StoreImageEntity.builder()
                    .imageUrl(imageUrl)
                    .build();
            images.add(storeImage);
        }
        storeEntity.setStoreImages(images); // 이미지 업데이트
    }

    // isOpen 값 유지
    if (storeUpdateDTO.getIsOpen() != null) {
        storeEntity.setIsOpen(storeUpdateDTO.getIsOpen());
    }

    // 카테고리 업데이트 (null 체크 추가)
    List<Integer> newCategoryPks = storeUpdateDTO.getCategoryPks(); // 새로운 카테고리 PK 리스트
    if (newCategoryPks != null) {
        updateStoreCategories(storePk, newCategoryPks); // 카테고리 업데이트 호출
    }

    // 주소가 변경된 경우 좌표 변환
    if (storeUpdateDTO.getAddress() != null && !storeUpdateDTO.getAddress().isEmpty()) {
        // 주소가 변경된 경우
        if (!storeEntity.getAddress().equals(storeUpdateDTO.getAddress())) {
            storeEntity.setAddress(storeUpdateDTO.getAddress()); // 주소 업데이트
            Double[] coordinates = geocodingService.getCoordinates(storeUpdateDTO.getAddress());
            if (coordinates != null) {
                storeEntity.setLat(coordinates[0]); // 위도 설정
                storeEntity.setLng(coordinates[1]); // 경도 설정
            } else {
                throw new RuntimeException("좌표를 찾을 수 없습니다. 입력한 주소를 확인하세요.");
            }
        }
    }

    // 매장 저장
    StoreEntity updatedStore = storeRepository.save(storeEntity);
    return convertToDTO(updatedStore); // DTO로 변환하여 반환
}


public void updateStoreStatus(Integer storePk, boolean isOpen) {
    // 매장 상태 업데이트 로직
    StoreEntity storeEntity = storeRepository.findById(storePk)
            .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다."));
    storeEntity.setIsOpen(isOpen);
    storeRepository.save(storeEntity);
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
    storeDTO.setCreatedAt(storeEntity.getCreatedAt());
 // Owner PK 설정
    if (storeEntity.getOwner() != null) {
        storeDTO.setOwnerPk(storeEntity.getOwner().getOwner_pk()); // owner_pk 설정
    }
    // 이미지 URL 리스트 변환
    List<String> imageUrls = storeEntity.getStoreImages() != null ? 
        storeEntity.getStoreImages().stream().map(StoreImageEntity::getImageUrl).toList() : 
        new ArrayList<>();
    storeDTO.setStoreImages(imageUrls);
    
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
    

    // OwnerEntity 설정
    OwnerEntity owner = new OwnerEntity();
    owner.setOwner_pk(storeDTO.getOwnerPk()); // owner_pk 설정
    storeEntity.setOwner(owner);

    // 이미지 저장 (이미지가 있는 경우에만)
    List<StoreImageEntity> images = new ArrayList<>();
    if (storeDTO.getStoreImages().size() > 0) {
        for (String imageUrl : storeDTO.getStoreImages()) {
            StoreImageEntity storeImage = StoreImageEntity.builder()
                    .imageUrl(imageUrl)
                    .build();
            
            images.add(storeImage);
        }
    }
    storeEntity.setStoreImages(images); 

    return storeEntity;
}

// 카테고리 업데이트 메소드
public void updateStoreCategories(Integer storePk, List<Integer> newCategoryPks) {
    List<Integer> existingCategoryPks = findCategoryPksByStoreId(storePk);

    // null 체크 추가
    if (newCategoryPks != null) {
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
    } else {
        logger.warn("새 카테고리 리스트가 null입니다. 매장 ID: {}", storePk);
    }
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
public boolean doesOwnerHaveStore(Integer ownerPk) {
    String jpql = "SELECT COUNT(s) FROM StoreEntity s WHERE s.owner.owner_pk = :ownerPk";
    Query query = entityManager.createQuery(jpql);
    query.setParameter("ownerPk", ownerPk);
    
    Long count = (Long) query.getSingleResult();
    return count > 0;
}

//소유자가 가진 매장을 찾는 메소드
public StoreDTO findStoreByOwnerId(Integer ownerPk) {
    OwnerEntity ownerEntity = new OwnerEntity();
    ownerEntity.setOwner_pk(ownerPk); // OwnerEntity 객체 생성 및 ID 설정

    StoreEntity storeEntity = storeRepository.findByOwner(ownerEntity)
            .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. 소유자 ID: " + ownerPk));

    return convertToDTO(storeEntity);
}
}