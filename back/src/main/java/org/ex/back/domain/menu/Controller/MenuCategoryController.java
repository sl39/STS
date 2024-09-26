package org.ex.back.domain.menu.Controller;

import lombok.RequiredArgsConstructor;
import org.ex.back.domain.menu.DTO.MenuCategoryRequestDTO;
import org.ex.back.domain.menu.DTO.MenuCategoryResponseDTO;
import org.ex.back.domain.menu.Service.MenuCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MenuCategoryController {
    //카테고리 리포지터리(서비스로) 연결
    private final MenuCategoryService menuCategoryService;

    //메뉴카테고리 생성
    @PostMapping("api/store/{id}")
    public ResponseEntity<MenuCategoryResponseDTO> createCategory(@PathVariable int id, @RequestBody MenuCategoryRequestDTO request) {
        MenuCategoryResponseDTO response =  menuCategoryService.createdCategory(id, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //메뉴카테고리 리스트 조회
    @GetMapping("api/store/{id}")
    public ResponseEntity<List<MenuCategoryResponseDTO>> getCategory(@PathVariable int id) {
        List<MenuCategoryResponseDTO> response = menuCategoryService.getCategory(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //메뉴카테고리 수정
    @PutMapping("api/category/{ctid}")
    public ResponseEntity<MenuCategoryResponseDTO> putCategory(@PathVariable int ctid, @RequestBody MenuCategoryRequestDTO request){
        MenuCategoryResponseDTO response = menuCategoryService.putCategory(ctid, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //메뉴 카테고리 삭제
    @DeleteMapping("api/category/{ctid}")
    public ResponseEntity<?> deleteCategory(@PathVariable int ctid){
        menuCategoryService.deleteCategory(ctid);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


    }

}
