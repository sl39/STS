package org.ex.back.domain.store.controller;

import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.dto.StoreUpdateDTO;
import org.ex.back.domain.store.model.StoreImageEntity;
import org.ex.back.domain.store.service.OwnerStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/store/owner")
public class OwnerStoreController {

    @Autowired
    private OwnerStoreService storeService;

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
            // isOpen 기본값 설정
            if (newStore.getIsOpen() == null) {
                newStore.setIsOpen(false);
            }

            // storeImages가 null인 경우 빈 리스트로 초기화
            if (newStore.getStoreImageUrls() == null) {
                newStore.setStoreImageUrls(new ArrayList<>()); // 빈 리스트 초기화
            }

            // 데이터 유효성 검사 (예: 운영 시간 길이 체크)
            if (newStore.getOperatingHours() != null && newStore.getOperatingHours().length() > 255) {
                return ResponseEntity.badRequest().body(null); // 400 Bad Request
            }

            // 매장 저장
            StoreDTO savedStore = storeService.saveStore(newStore);
            return ResponseEntity.status(201).body(savedStore); // 201 Created

        } catch (DataIntegrityViolationException e) {
            // 데이터베이스 제약 조건 위반 처리
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        } catch (RuntimeException e) {
            // 일반적인 런타임 예외 처리
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
            existingStore.setStorePk(storePk);
            
            // 기존 정보에서 필요한 값만 업데이트
            if (updatedStoreData.getStoreName() != null) {
                existingStore.setStoreName(updatedStoreData.getStoreName());
            }
            if (updatedStoreData.getAddress() != null) {
                existingStore.setAddress(updatedStoreData.getAddress());
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
            if (updatedStoreData.getLat() != null) {
                existingStore.setLat(updatedStoreData.getLat());
            }
            if (updatedStoreData.getLng() != null) {
                existingStore.setLng(updatedStoreData.getLng());
            }

            // 카테고리 업데이트
            if (updatedStoreData.getCategoryPks() != null) {
                storeService.updateStoreCategories(storePk, updatedStoreData.getCategoryPks());
            }

         // 이미지 업데이트
            if (updatedStoreData.getStoreImageUrls() != null) {
                existingStore.setStoreImageUrls(updatedStoreData.getStoreImageUrls()); // URL 리스트 설정
            }

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
            // 매장 정보를 먼저 가져옵니다.
            StoreDTO existingStore = storeService.findStoreById(storePk);
            
            // 현재 영업 상태를 반전시킵니다.
            existingStore.setIsOpen(!existingStore.getIsOpen());

            // 상태를 업데이트 후 저장합니다.
            StoreDTO updatedStore = storeService.saveStore(existingStore);
            return ResponseEntity.ok(updatedStore);
        } catch (RuntimeException e) {
            // 에러 로그
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
