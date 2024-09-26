package org.ex.back.domain.cart.Controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ex.back.domain.cart.DTO.CartItemDTO;
import org.ex.back.domain.cart.DTO.CartRequestDTO;
import org.ex.back.domain.cart.DTO.CartResponseDTO;
import org.ex.back.domain.cart.Service.CartService;
import org.ex.back.domain.menu.Service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Data
@RestController
public class CartNonUserController {

    private final CartService cartService;
    private final MenuService menuService;

    // 비회원 - 장바구니 생성하고 pk 반환
    @PostMapping("/api/cart/nonuser")
    public ResponseEntity<CartResponseDTO> createCart(){
        CartResponseDTO cartItemResponseDTO = cartService.creatCart();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartItemResponseDTO);
    }

    /*
            장바구니 CRUD
     */

    //장바구니에 장바구니 아이템 추가
    @PostMapping("/api/cart/nonuser/{cartId}")
    public ResponseEntity<CartResponseDTO> addCartItem(@PathVariable int cartId, @RequestBody CartRequestDTO request){
        CartResponseDTO response = cartService.pushCart(cartId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    //장바구니 메뉴 조회
    @GetMapping("/api/cart/nonuser/{cartId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable int cartId){
        CartResponseDTO response = cartService.getCart(cartId);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    //장바구니 메뉴 수량 수정
    @PatchMapping("/api/cart/nonuser/{cartId}/cartItem/{cartItemId}")
    public ResponseEntity<CartResponseDTO> updateCartItem(
            @PathVariable int cartId,
            @PathVariable int cartItemId,
            @RequestBody CartItemDTO request){

        CartResponseDTO response = cartService.putCart(cartId, cartItemId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    //장바구니 메뉴 삭제
    @DeleteMapping("/api/cart/nonuser/{cartId}/cartItem/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable int cartId,
            @PathVariable int cartItemId
    ){
        cartService.deleteCartItem(cartId, cartItemId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
