package org.ex.back.domain.store.controller;

import org.ex.back.domain.store.dto.ImageUrlsDTO;
import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.dto.StoreUpdateDTO;
import org.ex.back.domain.store.model.StoreImageEntity;
import org.ex.back.domain.store.service.GeocodingService;
import org.ex.back.domain.store.service.OwnerStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/store/owner")
public class OwnerStoreController {

    @Autowired
    private OwnerStoreService storeService;
    
    @Autowired
    private GeocodingService geocodingService;

    // 매장 조회 API
    @GetMapping("/{store_pk}")
    public ResponseEntity<Map<String, Object>> getStoreById(@PathVariable("store_pk") Integer storePk) {
        try {
            StoreDTO store = storeService.findStoreById(storePk);
            if (store != null) {
                List<Integer> categoryPks = storeService.findCategoryPksByStoreId(storePk);
                Map<String, Object> response = new HashMap<>();
                response.put("store", store);
                response.put("store_category_pks", categoryPks);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // 매장 등록 API
    @PostMapping
    public ResponseEntity<StoreDTO> createStore(@RequestBody StoreDTO newStore) {
        try {
            // 기본값 설정
            if (newStore.getIsOpen() == null) {
                newStore.setIsOpen(false);
            }
            if (newStore.getStoreImages() == null) {
                newStore.setStoreImages(new ArrayList<>()); // 빈 리스트 초기화
            }

            // 매장 저장
            StoreDTO savedStore = storeService.saveStore(newStore);

            // 이미지 저장을 위한 DTO 생성
            ImageUrlsDTO imageUrlsDTO = new ImageUrlsDTO(savedStore.getStorePk(), null, savedStore.getStoreImages());
            storeService.saveStoreImages(imageUrlsDTO); // 이미지 저장 메소드에서 처리

            return ResponseEntity.status(201).body(savedStore); // 201 Created

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PutMapping("/{store_pk}")
    public ResponseEntity<StoreDTO> updateStore(
            @PathVariable("store_pk") Integer storePk,
            @RequestBody StoreUpdateDTO updatedStoreData) {
        
        try {
            StoreDTO existingStore = storeService.findStoreById(storePk);
            if (existingStore == null) {
                return ResponseEntity.notFound().build(); // 매장이 존재하지 않을 경우
            }

            // 기존 createdAt 값을 유지
            LocalDateTime createdAt = existingStore.getCreatedAt();

            // 기존 정보에서 필요한 값만 업데이트
            if (updatedStoreData.getStoreName() != null) {
                existingStore.setStoreName(updatedStoreData.getStoreName());
            }
            
            if (updatedStoreData.getPhone() != null) {
                existingStore.setPhone(updatedStoreData.getPhone());
            }
            if (updatedStoreData.getOperatingHours() != null) {
                existingStore.setOperatingHours(updatedStoreData.getOperatingHours());
            }
            if (updatedStoreData.getStoreState() != null) {
                existingStore.setStoreState(updatedStoreData.getStoreState());
            }
            if (updatedStoreData.getIsOpen() != null) {
                existingStore.setIsOpen(updatedStoreData.getIsOpen());
            }
            // 이미지 업데이트
            List<String> updatedImages = new ArrayList<>();
            if (updatedStoreData.getStoreImages() != null) {
                for (String imageUrl : updatedStoreData.getStoreImages()) {
                    // URL 로그 추가
                    System.out.println("Received image URL: " + imageUrl);
                    imageUrl = imageUrl.trim(); // 앞뒤 공백 제거

                    if (!imageUrl.isEmpty()) {
                        updatedImages.add(imageUrl);
                    } else {
                        System.out.println("Image URL is null or empty for store ID: " + storePk);
                    }
                }
            }

            // 기존 이미지와 새 이미지를 합치기
            List<String> existingImages = existingStore.getStoreImages();
            if (existingImages != null) {
                updatedImages.addAll(existingImages);
            }

            existingStore.setStoreImages(updatedImages);

            // createdAt 값 다시 설정
            existingStore.setCreatedAt(createdAt); // 기존 createdAt 값 유지

            // 매장 저장
            StoreDTO savedStore = storeService.saveStore(existingStore);
            return ResponseEntity.ok(savedStore);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // 매장 영업 상태 전환 API
    @PutMapping("/{store_pk}/toggleStatus")
    public ResponseEntity<StoreDTO> toggleStoreStatus(@PathVariable("store_pk") Integer storePk) {
        try {
            StoreDTO existingStore = storeService.findStoreById(storePk);
            existingStore.setIsOpen(!existingStore.getIsOpen());
            StoreDTO updatedStore = storeService.saveStore(existingStore);
            return ResponseEntity.ok(updatedStore);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
}
