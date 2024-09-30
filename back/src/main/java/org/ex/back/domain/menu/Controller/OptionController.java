package org.ex.back.domain.menu.Controller;

import lombok.RequiredArgsConstructor;
import org.ex.back.domain.menu.DTO.OptionItemDTO;
import org.ex.back.domain.menu.DTO.OptionsDTO;
import org.ex.back.domain.menu.Service.OptionItemService;
import org.ex.back.domain.menu.Service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OptionController {
    private final OptionService optionService;
    private final OptionItemService optionItemService;


    //메뉴 옵션 생성 -> 완료
    @PostMapping("api/menu/{menuId}/option")
    public ResponseEntity<OptionsDTO> addOption(@PathVariable int menuId, @RequestBody OptionsDTO request) {
        OptionsDTO response = optionService.createdOption(menuId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    //메뉴 옵션 수정 -> 완료
    @PutMapping("api/option/{optionId}")
    public ResponseEntity<OptionsDTO> putOption(@PathVariable int optionId, @RequestBody OptionsDTO request) {
        OptionsDTO response = optionService.updateMenuOption(optionId,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    //메뉴 옵션 삭제
    @DeleteMapping("api/option/{optionId}")
    public ResponseEntity<?> deleteOption(@PathVariable int optionId) {
        optionService.deleteMenuOption(optionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //옵션 아이템 생성 -> 완료
    @PostMapping("api/optionItem/{optionId}")
    public ResponseEntity<OptionItemDTO> addOptionItem(@PathVariable int optionId, @RequestBody OptionItemDTO request) {
        OptionItemDTO response = optionItemService.createdOptionItem(optionId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    //옵션 아이템 수정
    @PutMapping("api/optionItem/{optionItemId}")
    public ResponseEntity<OptionItemDTO> putOptionItem(@PathVariable int optionItemId, @RequestBody OptionItemDTO request) {
        OptionItemDTO response = optionItemService.updateOptionItem(optionItemId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // 옵션 아이템 삭제
    @DeleteMapping("api/optionItem/{optionItemId}")
    public ResponseEntity<?> deleteOptionItem(@PathVariable int optionItemId) {
        optionItemService.deleteOptionItem(optionItemId);
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}
