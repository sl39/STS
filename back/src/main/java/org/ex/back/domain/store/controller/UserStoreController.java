package org.ex.back.domain.store.controller;

import org.ex.back.domain.store.dto.StoreDTO;
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
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        List<StoreDTO> stores = storeService.findAllStores();
        return ResponseEntity.ok(stores);
    }

 // 매장 이름 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<StoreDTO>> searchStores(
            @RequestParam("query") String query,
            @RequestParam("lat") double userLat,  // 위치 정보 추가
            @RequestParam("lng") double userLng) { // 위치 정보 추가
        List<StoreDTO> stores = storeService.searchStoresByName(query, userLat, userLng);
        return ResponseEntity.ok(stores);
    }

    // 특정 매장 정보 조회 API
    @GetMapping("/{store_id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable("store_id") Integer storeId) {
        try {
            StoreDTO store = storeService.findStoreById(storeId);
            return ResponseEntity.ok(store);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

 // 특정 카테고리에 해당하는 매장 조회 API
    @GetMapping("/category/{store_category_pk}")
    public ResponseEntity<List<StoreDTO>> getStoresByCategory(
            @PathVariable("store_category_pk") Integer storeCategoryPk,
            @RequestParam("lat") double userLat,  // 위치 정보 추가
            @RequestParam("lng") double userLng) { // 위치 정보 추가
        List<StoreDTO> stores = storeService.findStoresByCategory(storeCategoryPk, userLat, userLng);
        return ResponseEntity.ok(stores);
    }
}
