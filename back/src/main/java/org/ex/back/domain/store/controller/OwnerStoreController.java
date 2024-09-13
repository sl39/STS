package org.ex.back.domain.store.controller;

import org.ex.back.domain.store.model.StoreEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getStoreById(@PathVariable("store_pk") Integer storePk) {
        StoreEntity store = storeService.findStoreById(storePk);
        if (store != null) {
            // 연결된 카테고리 ID 리스트 가져오기
            List<Integer> categoryPks = storeService.findCategoryPksByStoreId(storePk);
            
            // 응답에 매장 정보와 카테고리 ID 포함
            Map<String, Object> response = new HashMap<>();
            response.put("store", store);
            response.put("store_category_pks", categoryPks);
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // 매장 업데이트 API
    @PutMapping("/{store_pk}")
    public ResponseEntity<StoreEntity> updateStore(
            @PathVariable("store_pk") Integer storePk,
            @RequestBody Map<String, Object> updatedStoreData) {
        
        StoreEntity existingStore = storeService.findStoreById(storePk);
        if (existingStore != null) {
            // ID 유지
            existingStore.setStore_pk(storePk);
            
            // 기존 정보에서 필요한 값만 업데이트
            if (updatedStoreData.containsKey("storeName")) {
                existingStore.setStoreName((String) updatedStoreData.get("storeName"));
            }
            if (updatedStoreData.containsKey("address")) {
                existingStore.setAddress((String) updatedStoreData.get("address"));
            }
            if (updatedStoreData.containsKey("phone")) {
                existingStore.setPhone((String) updatedStoreData.get("phone"));
            }
            if (updatedStoreData.containsKey("operatingHours")) {
                existingStore.setOperatingHours((String) updatedStoreData.get("operatingHours"));
            }
            if (updatedStoreData.containsKey("storeState")) {
                existingStore.setStoreState((String) updatedStoreData.get("storeState"));
            }
            if (updatedStoreData.containsKey("isOpen")) {
                existingStore.setIsOpen((Boolean) updatedStoreData.get("isOpen"));
            }
            if (updatedStoreData.containsKey("lat")) {
                existingStore.setLat((Double) updatedStoreData.get("lat"));
            }
            if (updatedStoreData.containsKey("lng")) {
                existingStore.setLng((Double) updatedStoreData.get("lng"));
            }

            // 카테고리 업데이트
            if (updatedStoreData.containsKey("categoryPks")) {
                List<Integer> newCategoryPks = (List<Integer>) updatedStoreData.get("categoryPks");
                storeService.updateStoreCategories(storePk, newCategoryPks);
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
}
