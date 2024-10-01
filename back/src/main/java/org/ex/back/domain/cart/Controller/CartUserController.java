package org.ex.back.domain.cart.Controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ex.back.domain.cart.DTO.CartItemDTO;
import org.ex.back.domain.cart.DTO.CartRequestDTO;
import org.ex.back.domain.cart.DTO.CartResponseDTO;
import org.ex.back.domain.cart.DTO.UserCartResponseDto;
import org.ex.back.domain.cart.Service.CartService;
import org.ex.back.domain.menu.Service.MenuService;
import org.ex.back.domain.user.service.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Data
@RestController
public class CartUserController {

    private final MenuService menuService;
    private final CartService cartService;

    // 회원 - 장바구니 pk 조회
    @GetMapping("/api/cart/user")
    public ResponseEntity<UserCartResponseDto> getUserCartPk(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        Integer userPk = userPrincipal.getUserPk();

      return new ResponseEntity<>(cartService.getUserCartPk(userPk), HttpStatus.OK);
    }

    /*
            장바구니 CRUD
     */

    //장바구니에 장바구니 아이템 추가
    @PostMapping("/api/cart/user/{cartId}")
    public ResponseEntity<CartResponseDTO> addCartItem(@PathVariable int cartId, @RequestBody CartRequestDTO request){
        CartResponseDTO response = cartService.pushCart(cartId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    //장바구니 메뉴 조회
    @GetMapping("/api/cart/user/{cartId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable int cartId){
        CartResponseDTO response = cartService.getCart(cartId);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    //장바구니 메뉴 수량 수정
    @PatchMapping("/api/cart/user/{cartId}/cartItem/{cartItemId}")
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
    @DeleteMapping("/api/cart/user/{cartId}/cartItem/{cartItemId}")
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
