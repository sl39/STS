package org.ex.back.domain.menu.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//아직 안함
@RequiredArgsConstructor
@RestController
public class UserMenuController {

    //사용자
    //유저의 가게별 메뉴 리스트 조회
    @GetMapping("api/store/{id}/user")
    public String getUserMenuList(@PathVariable int id) {
        return "아이디 전달 완료" + id;
    }
    //유저의 가게 메뉴 상세 조회
    @GetMapping("api/store/{id}/menu/{menuid}/user")
    public String getUserMenu(@PathVariable int id, @PathVariable int menuid) {
        return "스토어 id =" + id + "menu id = " + menuid;
    }
}
