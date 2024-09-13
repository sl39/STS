package org.ex.back.domain.store.controller;

import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreCategoryConnectorEntity;
import org.ex.back.domain.store.model.StoreCategoryEntity;
import org.ex.back.domain.store.service.UserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/user")
public class UserStoreController {

    @Autowired
    private UserStoreService storeService;

    // 모든 매장 조회 API
    @GetMapping
    public ResponseEntity<List<StoreEntity>> getAllStores() {
        List<StoreEntity> stores = storeService.findAllStores();
        return ResponseEntity.ok(stores);
    }

    // 매장 이름 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<StoreEntity>> searchStores(@RequestParam String query) {
        List<StoreEntity> stores = storeService.searchStoresByName(query);
        return ResponseEntity.ok(stores);
    }

    // 특정 매장 정보 조회 API
    @GetMapping("/{store_id}")
    public ResponseEntity<StoreEntity> getStoreById(@PathVariable("store_id") Integer storeId) {
        try {
            StoreEntity store = storeService.findStoreById(storeId);
            return ResponseEntity.ok(store);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 카테고리에 해당하는 매장 조회 API
    @GetMapping("/category/{store_category_pk}")
    public ResponseEntity<List<StoreEntity>> getStoresByCategory(@PathVariable("store_category_pk") Integer storeCategoryPk) {
        List<StoreEntity> stores = storeService.findStoresByCategory(storeCategoryPk);
        return ResponseEntity.ok(stores);
    }

    // 특정 매장의 카테고리 조회 API
    @GetMapping("/{store_id}/categories") // 경로 변경
    public ResponseEntity<List<Integer>> getCategoriesByStoreId(@PathVariable("store_id") Integer storePk) {
        List<Integer> categories = storeService.findCategoriesByStoreId(storePk);
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(categories);
    }
}

