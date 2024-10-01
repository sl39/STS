package org.ex.back.domain.menu.Controller;

import lombok.RequiredArgsConstructor;
import org.ex.back.domain.menu.DTO.*;
import org.ex.back.domain.menu.Service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 요청 API에 따라(사용자, 판매자) CRUD
@RequiredArgsConstructor
@RestController
public class MenuController {
    //Menu별 서비스 연결
    private final MenuService menuService;


    //판매자
    //메뉴 생성
    @PostMapping("api/store/{id}/menu")
    public ResponseEntity<MenuResponseDTO> addCategory(@PathVariable int id, @RequestBody MenuRequestDTO request) {
        MenuResponseDTO response = menuService.createdMenu(id, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    //메뉴리스트 조회
    @GetMapping("api/store/{id}/menu")
    public ResponseEntity<List<MenuResponseDTO>> getMenuList(@PathVariable int id) {
        List<MenuResponseDTO> response = menuService.getMenuList(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    //메뉴 상세 조회
    @GetMapping("api/menu/{menuid}/menu")
    public ResponseEntity<MenuResponseDTO> getMenu(@PathVariable int menuid) {
        MenuResponseDTO response = menuService.getMenu(menuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    //메뉴 수정
    @PutMapping("api/menu/{menuid}/menu")
    public ResponseEntity<MenuResponseDTO> putMenu(@PathVariable int menuid, @RequestBody MenuRequestDTO request) {
        MenuResponseDTO response = menuService.putMenu( menuid, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    //메뉴 삭제
    @DeleteMapping("api/menu/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable int menuId) {
        menuService.deleteMenu(menuId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
