package org.ex.back.domain.store.controller;

import org.ex.back.domain.store.model.StoreEntity;

import java.util.List;

import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.service.OwnerStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store/owner")
public class OwnerStoreController {

    @Autowired
    private OwnerStoreService storeService;

    // 매장 조회 API
    @GetMapping("/{store_pk}")
    public ResponseEntity<StoreEntity> getStoreById(@PathVariable("store_pk") Integer storePk) {
        StoreEntity store = storeService.findStoreById(storePk);
        if (store != null) {
            return ResponseEntity.ok(store);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 매장 업데이트 API
    @PutMapping("/{store_pk}")
    public ResponseEntity<StoreEntity> updateStore(@PathVariable("store_pk") Integer storePk, @RequestBody StoreEntity updatedStore) {
        StoreEntity existingStore = storeService.findStoreById(storePk);
        if (existingStore != null) {
            // ID 유지
            updatedStore.setStore_pk(storePk);
            
            // 기존 정보에서 필요한 값만 업데이트
            if (updatedStore.getStoreName() != null) {
                existingStore.setStoreName(updatedStore.getStoreName());
            }
            if (updatedStore.getAddress() != null) {
                existingStore.setAddress(updatedStore.getAddress());
            }
            if (updatedStore.getPhone() != null) {
                existingStore.setPhone(updatedStore.getPhone());
            }
            if (updatedStore.getOperatingHours() != null) {
                existingStore.setOperatingHours(updatedStore.getOperatingHours());
            }
            if (updatedStore.getStoreState() != null) {
                existingStore.setStoreState(updatedStore.getStoreState());
            }
            if (updatedStore.getIsOpen() != null) {
                existingStore.setIsOpen(updatedStore.getIsOpen());
            }
            if (updatedStore.getLat() != null) {
                existingStore.setLat(updatedStore.getLat());
            }
            if (updatedStore.getLng() != null) {
                existingStore.setLng(updatedStore.getLng());
            }
            StoreEntity savedStore = storeService.saveStore(existingStore);
            return ResponseEntity.ok(savedStore);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 매장 영업 상태 전환 API
    @PutMapping("/{store_pk}/toggleStatus")
    public ResponseEntity<StoreEntity> toggleStoreStatus(@PathVariable("store_pk") Integer storePk) {
        StoreEntity updatedStore = storeService.toggleStoreOpenStatus(storePk);
        return ResponseEntity.ok(updatedStore);
    }

 // 매장에 여러 카테고리 추가 API
    @PostMapping("/{store_pk}/categories")
    public ResponseEntity<String> addCategoriesToStore(@PathVariable("store_pk") Integer storePk, @RequestBody List<Integer> categoryPks) {
        StoreEntity store = storeService.findStoreById(storePk);
        if (store != null) {
            for (Integer categoryPk : categoryPks) {
                StoreCategoryEntity category = storeService.findCategoryById(categoryPk);
                if (category != null) {
                    storeService.addCategoryToStore(storePk, categoryPk);  // 카테고리 추가 메소드 호출
                } else {
                    return ResponseEntity.status(404).body("카테고리 ID " + categoryPk + "를 찾을 수 없습니다.");
                }
            }
            return ResponseEntity.ok("카테고리가 매장에 추가되었습니다.");
        } else {
            return ResponseEntity.status(404).body("매장을 찾을 수 없습니다.");
        }
    }
}
