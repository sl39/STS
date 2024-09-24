package org.ex.back.domain.store.controller;

import org.ex.back.domain.store.dto.ImageUrlsDTO;
import org.ex.back.domain.store.dto.ResponseDTO;
import org.ex.back.domain.store.dto.StoreDTO;
import org.ex.back.domain.store.dto.StoreUpdateDTO;
import org.ex.back.domain.store.service.GeocodingService;
import org.ex.back.domain.store.service.OwnerStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
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
            List<Integer> categoryPks = storeService.findCategoryPksByStoreId(storePk);
            Map<String, Object> response = new HashMap<>();
            response.put("store", store);
            response.put("categoryPks", categoryPks);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "매장 조회 중 오류가 발생했습니다."));
        }
    }

    // 매장 등록 API
    @PostMapping("/{owner_pk}")
    public ResponseEntity<StoreDTO> createStore(
            @PathVariable("owner_pk") Integer ownerPk, // owner_pk를 PathVariable로 받음
            @RequestBody StoreDTO newStore) {
        try {
            // 소유자가 매장을 가지고 있는지 확인
            boolean hasStore = storeService.doesOwnerHaveStore(ownerPk);
            if (hasStore) {
                return ResponseEntity.badRequest().body(null); // 이미 매장이 있을 경우 등록 실패
            }

            // owner_pk 설정
            newStore.setOwnerPk(ownerPk); // ownerPk 설정

            // 기본값 설정
            if (newStore.getIsOpen() == null) {
                newStore.setIsOpen(false);
            }
            if (newStore.getStoreImages() == null) {
                newStore.setStoreImages(new ArrayList<>()); // 빈 리스트 초기화
            }

            // 주소를 좌표로 변환
            if (newStore.getAddress() != null && !newStore.getAddress().isEmpty()) {
                Double[] coordinates = geocodingService.getCoordinates(newStore.getAddress());
                if (coordinates != null) {
                    newStore.setLat(coordinates[0]); // 위도 설정
                    newStore.setLng(coordinates[1]); // 경도 설정
                    System.out.println("저장할 좌표: 위도 = " + newStore.getLat() + ", 경도 = " + newStore.getLng());
                } else {
                    return ResponseEntity.badRequest().body(null); // 좌표 찾기 실패
                }
            }

            // 매장 저장
            StoreDTO savedStore = storeService.saveStore(newStore);
            return ResponseEntity.status(201).body(savedStore); // 201 Created와 함께 저장된 매장 정보 반환
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null); // 데이터 무결성 오류
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 일반 오류
        }
    }
    @PutMapping("/{store_pk}")
    public ResponseEntity<Map<String, Object>> updateStore(
            @PathVariable("store_pk") Integer storePk,
            @RequestBody StoreUpdateDTO updatedStoreData) {

        try {
            StoreDTO existingStore = storeService.findStoreById(storePk);
            if (existingStore == null) {
                return ResponseEntity.notFound().build(); // 매장이 존재하지 않을 경우
            }

            // 업데이트된 정보 설정 (이름, 전화번호, 운영시간이 없으면 기존 값 유지)
            if (updatedStoreData.getStoreName() != null && !updatedStoreData.getStoreName().isEmpty()) {
                existingStore.setStoreName(updatedStoreData.getStoreName());
            }

            if (updatedStoreData.getPhone() != null && !updatedStoreData.getPhone().isEmpty()) {
                existingStore.setPhone(updatedStoreData.getPhone());
            }

            if (updatedStoreData.getOperatingHours() != null && !updatedStoreData.getOperatingHours().isEmpty()) {
                existingStore.setOperatingHours(updatedStoreData.getOperatingHours());
            }

            existingStore.setStoreState(updatedStoreData.getStoreState());

            // 이미지 업데이트
            if (updatedStoreData.getStoreImages() != null) {
                existingStore.setStoreImages(updatedStoreData.getStoreImages()); // 이미지 업데이트
            }

            // isOpen 값 유지
            if (updatedStoreData.getIsOpen() != null) {
                existingStore.setIsOpen(updatedStoreData.getIsOpen());
            }

            // 주소가 업데이트되었을 경우 좌표 재계산
            if (updatedStoreData.getAddress() != null && !updatedStoreData.getAddress().isEmpty()) {
                if (!existingStore.getAddress().equals(updatedStoreData.getAddress())) { 
                    existingStore.setAddress(updatedStoreData.getAddress()); // 주소 업데이트
                    Double[] coordinates = geocodingService.getCoordinates(updatedStoreData.getAddress());
                    if (coordinates != null) {
                        existingStore.setLat(coordinates[0]);
                        existingStore.setLng(coordinates[1]);
                    } else {
                        return ResponseEntity.badRequest().body(Map.of("error", "좌표 계산 실패")); // 좌표 계산 실패 시 오류 메시지 반환
                    }
                }
            }

            // 카테고리 업데이트
            if (updatedStoreData.getCategoryPks() != null) {
                storeService.updateStoreCategories(storePk, updatedStoreData.getCategoryPks());
            }

            // 매장 저장
            StoreDTO updatedStore = storeService.updateStore(storePk, updatedStoreData); 

            // 카테고리 정보 가져오기
            List<Integer> categoryPks = storeService.findCategoryPksByStoreId(storePk);

            // 응답 구성
            Map<String, Object> response = new HashMap<>();
            response.put("store", updatedStore);
            response.put("categoryPks", categoryPks); // store_category_pks를 categoryPks로 변경

            return ResponseEntity.ok(response); // 업데이트된 매장 정보와 카테고리 정보 반환
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "데이터 무결성 오류")); // 데이터 무결성 오류 처리
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "서버 오류")); // 일반 오류 처리
        }
    }

    @PutMapping("/{store_pk}/toggleStatus")
    public ResponseEntity<Map<String, Object>> toggleStoreStatus(@PathVariable("store_pk") Integer storePk) {
        try {
            // 매장 ID로 매장 조회
            StoreDTO existingStore = storeService.findStoreById(storePk);
            if (existingStore == null) {
                return ResponseEntity.notFound().build(); // 매장이 존재하지 않을 경우
            }

            // 영업 상태 전환
            existingStore.setIsOpen(!existingStore.getIsOpen());

            // 매장 상태만 업데이트
            storeService.updateStoreStatus(storePk, existingStore.getIsOpen());

            // 응답 구성: 변경된 상태만 반환
            Map<String, Object> response = new HashMap<>();
            response.put("storePk", existingStore.getStorePk());
            response.put("isOpen", existingStore.getIsOpen());

            return ResponseEntity.ok(response); // 변경된 매장 상태를 응답으로 반환
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "매장 상태 전환 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("/{owner_pk}/hasStore")
    public ResponseEntity<Boolean> hasStore(@PathVariable("owner_pk") Integer ownerPk) {
        boolean hasStore = storeService.doesOwnerHaveStore(ownerPk);
        return ResponseEntity.ok(hasStore);
    }
}
