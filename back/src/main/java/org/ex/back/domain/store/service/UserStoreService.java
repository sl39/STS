package org.ex.back.domain.store.service;

import org.ex.back.domain.menu.Repository.MenuRepository;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreImageEntity;
import org.ex.back.domain.store.repository.StoreCategoryConnectorRepository;
import org.ex.back.domain.store.repository.UserStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStoreService {

    @Autowired
    private UserStoreRepository storeRepository;

    @Autowired
    private StoreCategoryConnectorRepository storeCategoryConnectorRepository;
    
    @Autowired
    private MenuRepository menuRepository;  
    
 // 모든 매장을 조회하는 메소드 (위치 기반 정렬 추가)
    public List<StoreDTO> findAllStores(double userLat, double userLng) {
        List<StoreDTO> stores = storeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return sortStoresByDistance(stores, userLat, userLng);
    }
 // 매장 ID로 연결된 카테고리 ID 리스트 가져오기
    public List<Integer> findCategoryPksByStoreId(Integer storePk) {
        return storeCategoryConnectorRepository.findCategoryPksByStoreId(storePk);
    }

 // 매장 이름 또는 메뉴 이름으로 매장을 검색하는 메소드
    public List<StoreDTO> searchStores(String query, double userLat, double userLng) {
        // 매장 이름 검색 (부분 검색)
        List<StoreDTO> storesByName = storeRepository.findByStoreNameContaining(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 매장 이름 검색 (정확히 일치)
        List<StoreDTO> exactStoresByName = storeRepository.findByStoreName(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

     // 메뉴 검색 (부분 검색)
        List<MenuEntity> menusByPartialName = menuRepository.findByNameContaining(query);
        List<Integer> storePksFromPartialMenus = menusByPartialName.stream()
            .map(menu -> {
                if (menu.getStore() != null) {
                    return menu.getStore().getStore_pk();
                }
                return null; // null인 경우 처리
            })
            .filter(storePk -> storePk != null) // null 제거
            .distinct()
            .collect(Collectors.toList());
        
     // 메뉴 검색 (정확히 일치)
        List<MenuEntity> menusByExactName = menuRepository.findByName(query);
        List<Integer> storePksFromExactMenus = menusByExactName.stream()
            .map(menu -> {
                if (menu.getStore() != null) {
                    return menu.getStore().getStore_pk();
                }
                return null; // null인 경우 처리
            })
            .filter(storePk -> storePk != null) // null 제거
            .distinct()
            .collect(Collectors.toList());

        // 메뉴로 찾은 매장 DTO들
        List<StoreDTO> storesByMenu = new ArrayList<>();
        storesByMenu.addAll(storePksFromPartialMenus.stream()
                                                    .map(this::findStoreById)
                                                    .collect(Collectors.toList()));
        storesByMenu.addAll(storePksFromExactMenus.stream()
                                                  .map(this::findStoreById)
                                                  .collect(Collectors.toList()));

     // 모든 검색 결과 통합
        List<StoreDTO> combinedStores = new ArrayList<>(storesByName);
        combinedStores.addAll(exactStoresByName);
        combinedStores.addAll(storesByMenu);

        // 중복 제거 및 거리 기준으로 정렬
        return combinedStores.stream()
                             .distinct()
                             .filter(store -> store.getLat() != null && store.getLng() != null) // null 체크
                             .map(store -> {
                                 double distance = calculateDistance(userLat, userLng, store.getLat(), store.getLng());
                                 store.setDistance(distance); // 거리 설정
                                 return store;
                             })
                             .sorted(Comparator.comparingDouble(StoreDTO::getDistance))
                             .limit(50)
                             .collect(Collectors.toList());
    }

    // 매장 ID로 매장을 찾는 메소드
    public StoreDTO findStoreById(Integer storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다. ID: " + storeId));
        StoreDTO storeDTO = convertToDTO(storeEntity); // 매장 정보를 DTO로 변환

        // 카테고리 ID 리스트 조회
        List<Integer> categoryPks = findCategoryPksByStoreId(storeId); // 카테고리 ID 조회 메서드 호출
        storeDTO.setCategoryPks(categoryPks); // 카테고리 정보를 DTO에 설정

        return storeDTO;
    }

    // 특정 카테고리에 해당하는 매장을 조회하는 메소드
    public List<StoreDTO> findStoresByCategory(Integer storeCategoryPk, double userLat, double userLng) {
        List<StoreDTO> stores = storeRepository.findStoresByCategory(storeCategoryPk).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return sortStoresByDistance(stores, userLat, userLng);
    }

    // 사용자 위치를 기반으로 가까운 매장을 정렬하는 메소드
    private List<StoreDTO> sortStoresByDistance(List<StoreDTO> stores, double userLat, double userLng) {
        return stores.stream()
            .filter(store -> store.getLat() != null && store.getLng() != null) // null 체크 추가
            .map(store -> {
                double distance = calculateDistance(userLat, userLng, store.getLat(), store.getLng());
                store.setDistance(distance); // 거리 설정
                return store;
            })
            .sorted(Comparator.comparingDouble(StoreDTO::getDistance)) // 거리 기준으로 정렬
            .limit(50) // 최대 50개까지
            .collect(Collectors.toList());
    }


    // 사용자 위치를 기반으로 가까운 매장을 검색하는 메소드
    public List<StoreDTO> findNearestStores(List<StoreDTO> stores, double userLat, double userLng) {
        return stores.stream()
            .filter(store -> store.getLat() != null && store.getLng() != null) // null 체크 추가
            .map(store -> {
                double distance = calculateDistance(userLat, userLng, store.getLat(), store.getLng());
                store.setDistance(distance);
                return store;
            })
            .sorted(Comparator.comparingDouble(StoreDTO::getDistance))
            .limit(50)
            .collect(Collectors.toList());
    }

    // 두 지점 간의 거리 계산 (Haversine 공식을 사용)
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // 지구 반경 (킬로미터)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 반환 (킬로미터)
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

}
