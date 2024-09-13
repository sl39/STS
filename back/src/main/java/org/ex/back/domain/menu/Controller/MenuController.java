package org.ex.back.domain.menu.Controller;

import lombok.RequiredArgsConstructor;
import org.ex.back.domain.menu.Service.MenuService;
import org.springframework.web.bind.annotation.RestController;

// 요청 API에 따라(사용자, 판매자) 판매자 입장에서의 CRUD, 사용자 입장에서의 R 로직 작성
@RequiredArgsConstructor
@RestController
public class MenuController {
    private final MenuService menuService;
}
