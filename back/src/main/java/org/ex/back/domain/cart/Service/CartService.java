package org.ex.back.domain.cart.Service;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.cart.DTO.CartItemDTO;
import org.ex.back.domain.cart.DTO.CartRequestDTO;
import org.ex.back.domain.cart.DTO.CartResponseDTO;
import org.ex.back.domain.cart.DTO.UserCartResponseDto;
import org.ex.back.domain.cart.repository.CartItemRepository;
import org.ex.back.domain.cart.model.CartEntity;
import org.ex.back.domain.cart.model.CartItemEntity;
import org.ex.back.domain.cart.repository.CartRepository;
import org.ex.back.domain.menu.Repository.MenuRepository;
import org.ex.back.domain.menu.model.MenuEntity;
import org.ex.back.domain.store.repository.StoreRepository;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.user.repository.UserRepository;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Data
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    //TotalPrice 연산 메소드 (현재 존재하는 옵션 값, 메뉴 가격, 메뉴 수량)
    private Integer convertTotalPrice(Integer totalExtraPrice, Integer menuPrice, Integer menuCount) {
        //아이템 리스트의 토탈 프라이스 계산후 리턴(존재하는 옵션값 + 해당 아이템 메뉴 가격) * 아이템 수량
        Integer totalPrice = (totalExtraPrice + menuPrice) * menuCount;
        return totalPrice;
    }

    //장바구니 생성(pk 반환)
    public CartResponseDTO creatCart() {
        //cart_pk 생성을 위한 저장
        CartEntity cartEntity = new CartEntity();
        cartEntity = cartRepository.save(cartEntity);

        //cartEntity 내용을 CartResponseDTO로 변환
        CartResponseDTO response = new CartResponseDTO();
        response.setCart_pk(cartEntity.getCart_pk());

        return response;
    }

    //징바구니 메뉴 추가 (요청받은 cartId 가져와서 해당 카트 엔티티에 추가, 메뉴 조회후 추가할때 이거 같이 써도 될듯)
    @Transactional
    public CartResponseDTO pushCart(int cart_pk, CartRequestDTO request) {

        /* Cart 세팅*/

        //일치하는 장바구니 가져오기 (없으면 에러)
        CartEntity cart = cartRepository.findById(cart_pk).orElseThrow(
                () -> new CustomException(ErrorCode.CART_NOT_FOUND)
        );

       //스토어 정보가 없거나 기존의 스토어와 요청 스토어가 다른 경우 내용 비우기 -> 스토어정복가 없을때 아래 내용 지워도 상관X
        if (cart.getStore() == null || !cart.getStore().getStore_pk().equals(request.getStore_pk())) {

            //총 메뉴 합을 0으로 변경 -> 카트의 내용이 사라지므로
            cart.setTotalPrice(0);

            //스토어를 null로 만들기 -> 밑의 로직에 끼워넣으려고
            cart.setStore(null);

            //존재하는 카트 아이템 비우기
            cart.getCartItems().clear();

            //바뀐 카트 저장
            cartRepository.save(cart); //여기서 저장 안해줘도 될듯??  -> 밑에서 추가하고 저장하면 되니까
        }

        //스토어 리포지 터리에서 일치하는 pk 가져와서 넣기 -> 없으면 에러
        StoreEntity store = storeRepository.findById(request.getStore_pk()).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );

        //요청받은 메뉴 값 가져오기(현재 메뉴 값이 null이라 pk로 받아서 넣어줘야함)
        MenuEntity menu = new MenuEntity();
        menu = menuRepository.findById(request.getMenu_pk()).orElseThrow((
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        ));

        //카트의 Store를 찾은 가게정보로 바꿈
        //해당 카트의 테이블 넘버를 요청받은 테이블 넘버로 바꿈
        cart.setStore(store);
        cart.setTableNumber(request.getTableNumber());

        //제일처음 값을 저장할 아이템 객체
        CartItemEntity cartItemEntity = new CartItemEntity();

        //카트 내부의 아이템들이 비어있는 경우 -> 처음 담길때나 위의 로직에서 카트 아이쳄이 사라진 경우
        //request받은 menupk가 기존 아이템 의 menuPk와 일치 하지 않는 경우
        //비었거나 현재 request로 받아온 카트 아이템이 현재 존재하는 카트 아이템 엔티티에 포함되지 않는 경우
        if(cart.getCartItems().isEmpty()) {

            cartItemEntity.setOptionItemList(request.getOptionItemList()); //엔티티 내부의 옵션 리스트를 요청 옵션 리스트로 변경
            cartItemEntity.setMenuCount(1);// 하나씩 추가되기 때문 (비어있을때는 젤 처음 1개만 들어감)
            cartItemEntity.setTotalExtraPrice(request.getTotalExtraPrice()); //옵션값 합을 요청 옵션값 합으로 변경
            cartItemEntity.setTotalPrice(convertTotalPrice(request.getTotalExtraPrice(), menu.getPrice(), 1));
            cartItemEntity.setMenu(menu); //엔티티 내부의 메뉴를 요청 메뉴로 변경

            //카트 내부에 저장
            //값이 바뀔때마다 리포지 터리 저장 -> 저장 안해주면 또 새로 바뀌니까? -> 근데 나중에 해줘도 상관없다고 생각함
            cartItemRepository.save(cartItemEntity);

            //카트의 아이템 리스트에 추가
            cart.getCartItems().add(cartItemEntity);
            //만들어진 값들을 카트에 저장 (스토어, 테이블 넘버, 아이템리스트 하나) 들어가있음
            cartRepository.save(cart);

        }else
        //카트 아이템이 비어있지 않은 경우 로직
        {

            //이제 여기다 for문 사용 (카트 아이템 안에 무조건 값이 있어야 실행 되므로)
            //하 가져온 리스트가 비어있으면 for문은 실행 안됨
            //아이템 리스트가 비어있지 않은 경우에 for문 써야하네

            /* Cart Item 세팅 */

            // 1. 같은 메뉴에 옵션까지 같을 때 ->
            //      CartItem - 수량, 가격 수정
            //      Cart - 가격 수정
            //이제 다른 값들을 넣어줘야 하는데 이때 반복문으로 넣돼 조건문을 사용
            //조건문으로 카트에 내용 추가하기(카트의 카트 아이템에 넣기 위한 아이템들), 기존의 카트 순회
            //cart.getCartItems()) => {아이템pk, 메뉴pk 등등}, {//} ,{//} 하나의 객체안에 여러값 들어있음

            //복사된 리스트를 순회하면서 add로 기존의 리스트에 추가?
            List<CartItemEntity> equalCartItem = new ArrayList<>(cart.getCartItems());

            //일단 메뉴 값이 들어있음
            log.info("equalCartItem: {}", equalCartItem);
            boolean itemExists = false;

            for (CartItemEntity cartItem : equalCartItem ) {

                //조건 충족 하는 경우 (메뉴pk 동일, 옵션아이템리스트 동일)
                if (cartItem.getMenu().getMenu_pk() == request.getMenu_pk() && cartItem.getOptionItemList().equals(request.getOptionItemList())) {

                    //해당 카트 아이템 엔티티의 메뉴 카운트 (기존 메뉴 카운트에 +1 증가로 변경);
                    cartItem.setMenuCount(cartItem.getMenuCount() + 1);

                    //해당 아이템 토탈 프라이스 값 변경 (해당 카트 아이템의 옵션값과 메뉴값과 메뉴 수량이 곱해진 값으로 변경)
                    cartItem.setTotalPrice(convertTotalPrice(cartItem.getTotalExtraPrice(), cartItem.getMenu().getPrice(), cartItem.getMenuCount()));

                    //값이 바뀔때마다 리포지 터리 저장 -> 저장 안해주면 또 새로 바뀌니까? -> 근데 나중에 해줘도 상관없다고 생각함
                    cartItemRepository.save(cartItem);

                    //값이 바뀔때마다 카트의 totalPrice도 업데이트 (카트 내 모든 아이템의 totalPrice를 합산)
                    Integer totalCartPrice = cart.getCartItems().stream()
                            .mapToInt(CartItemEntity::getTotalPrice)
                            .sum();

                    //값이 바뀔때마다 카트의 내의 값 변경 -> 카트의 totalPrice도 계속 바뀌니까
                    cart.setTotalPrice(totalCartPrice);

                    //만들어진 값들을 카트에 저장
                    cartRepository.save(cart);

                    log.info("cart22: {}", cart);
                    itemExists = true; //이 조건문이 실행되면 true가 됨
                    break;

                }
            }
            if(!itemExists){
                //조건을 충족하지 않는 경우
                //새로운 아이템 엔티티의 객체를 만들고 값 수정후 Count 1로 추가,
                CartItemEntity elseCartItem = new CartItemEntity();
                elseCartItem.setMenu(menu);
                elseCartItem.setOptionItemList(request.getOptionItemList());
                elseCartItem.setMenuCount(1);
                elseCartItem.setTotalExtraPrice(request.getTotalExtraPrice());
                elseCartItem.setTotalPrice(convertTotalPrice(request.getTotalExtraPrice(), menu.getPrice(), 1));

                cartItemRepository.save(elseCartItem);

                log.info("Saved elseCartItem with PK: {}", elseCartItem.getCart_item_pk());

                //리스트를 순회하는 중에 add를 하면 무한 반복으로 순회해야할 리스트가 늘어나기 때문에 사용 불가
                //복사된 리스트를 순회하면서 기존의 리스트에 값 추가
                cart.getCartItems().add(elseCartItem);

                //값이 바뀔때마다 카트의 totalPrice도 업데이트 (카트 내 모든 아이템의 totalPrice를 합산)
                Integer totalCartPrice = cart.getCartItems().stream()
                        .mapToInt(CartItemEntity::getTotalPrice)
                        .sum();

                //값이 바뀔때마다 카트의 내의 값 변경 -> 카트의 totalPrice도 계속 바뀌니까
                cart.setTotalPrice(totalCartPrice);

                //만들어진 값들을 카트에 저장
                cartRepository.save(cart);
                log.info("cart33: {}", cart);


            }
        }

        //카트 엔티티가 완성됨 (어차피 위에서 둘중 하나가 실행되기 때문에 엔티티 변환 작업은 반복문 밖에서 한번만해주자)
        //만들어진 카트 와 아이템을 CartResponse타입으로 반환
        CartResponseDTO response = new CartResponseDTO(
                cart.getCart_pk(),
                cart.getStore().getStore_pk(),
                cart.getStore().getStoreName(),
                cart.getTableNumber(),
                cart.getTotalPrice(),
                cart.getCartItems().stream().map(
                        item -> new CartItemDTO(
                                item.getCart_item_pk(),
                                item.getMenu().getMenu_pk(),
                                item.getMenu().getName(),
                                item.getOptionItemList(),
                                item.getMenuCount(),
                                item.getTotalPrice()
                        )
                ).collect(Collectors.toList())
        );

        log.info("response: {}", response);
        return response;

    }

    //장바구니 메뉴 조회
    public CartResponseDTO getCart( int cartID){
        //받은 카트 아이디 정보 받아오기
        Optional<CartEntity> cartEntity = cartRepository.findById(cartID);
        if(cartEntity.isPresent()) {
            CartEntity cart = cartEntity.get();
            //카트 정보를 reponseDTO로 변환
            CartResponseDTO response = new CartResponseDTO(
                    cart.getCart_pk(),
                    cart.getStore().getStore_pk(),
                    cart.getStore().getStoreName(),
                    cart.getTableNumber(),
                    cart.getTotalPrice(),
                    cart.getCartItems().stream().map(
                            item -> new CartItemDTO(
                                    item.getCart_item_pk(),
                                    item.getMenu().getMenu_pk(),
                                    item.getMenu().getName(),
                                    item.getOptionItemList(),
                                    item.getMenuCount(),
                                    item.getTotalPrice()
                            )
                    ).collect(Collectors.toList())
            );
            return response;

        }
        else{
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }
    }
    //장바구니 메뉴 수량 수정
    public CartResponseDTO putCart(Integer cartId, Integer cartItemId, CartItemDTO request) {
        //해당 카트 가져오기
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
        if(cartEntity.isPresent()) {
            //카트 아이템과 정보가 들어있는 카트 정보 받아오기
            CartEntity cart = cartEntity.get();

            Optional<CartItemEntity> cartItemEntity = cartItemRepository.findById(cartItemId);
            if(cartItemEntity.isPresent()) {

                //수량 변경할 아이템 정보 받아오기
                CartItemEntity cartItem = cartItemEntity.get();

                //해당 카트아이템의 메뉴 카운트를 요청받은 카트 아이템 값으로 변경
                cartItem.setMenuCount(request.getMenuCount());

                //수량 변경에 따라 아이템 totalPrice 변경
                cartItem.setTotalPrice(convertTotalPrice(cartItem.getTotalExtraPrice(), cartItem.getMenu().getPrice(), request.getMenuCount()));


                cartItemRepository.save(cartItem);


                //값이 바뀔때마다 카트의 totalPrice도 업데이트 (카트 내 모든 아이템의 totalPrice를 합산)
                Integer totalCartPrice = cart.getCartItems().stream()
                        .mapToInt(CartItemEntity::getTotalPrice)
                        .sum();

                cart.setTotalPrice(totalCartPrice);
                cartRepository.save(cart);

            }
            //카트 정보를 reponseDTO로 변환
            CartResponseDTO response = new CartResponseDTO(
                    cart.getCart_pk(),
                    cart.getStore().getStore_pk(),
                    cart.getStore().getStoreName(),
                    cart.getTableNumber(),
                    cart.getTotalPrice(),
                    cart.getCartItems().stream().map(
                            item -> new CartItemDTO(
                                    item.getCart_item_pk(),
                                    item.getMenu().getMenu_pk(),
                                    item.getMenu().getName(),
                                    item.getOptionItemList(),
                                    item.getMenuCount(),
                                    item.getTotalPrice()
                            )
                    ).collect(Collectors.toList())
            );
            return response;
        }
        else{
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }
    }

    //장바구니 메뉴 삭제 return -> 리스트
    public void deleteCartItem(Integer cartId, Integer cartItemId) {
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
        if(cartEntity.isPresent()) {
            CartEntity cart = cartEntity.get();
            Optional<CartItemEntity> cartItemEntity = cartItemRepository.findById(cartItemId);
            if(cartItemEntity.isPresent()) {
                CartItemEntity cartItem = cartItemEntity.get();

                //먼저 카트의 아이템 리스트에서 해당 아이템을 찾아서 삭제
                for(CartItemEntity cartItemList : cart.getCartItems()) {
                    if(cartItemList == cartItem) {
                        cart.getCartItems().remove(cartItem);
                    }
                    break;
                }

                //카트 아이템 리포지 터리에서 삭제
                cartItemRepository.delete(cartItem);

                //카트 total Price 업데이트
                //값이 바뀔때마다 카트의 totalPrice도 업데이트 (카트 내 모든 아이템의 totalPrice를 합산)
                Integer totalCartPrice = cart.getCartItems().stream()
                        .mapToInt(CartItemEntity::getTotalPrice)
                        .sum();

                cart.setTotalPrice(totalCartPrice);
                cartRepository.save(cart);
            }
        }
        else{
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }
    }

    public UserCartResponseDto getUserCartPk(Integer userPk) {

        UserEntity user = userRepository.findById(userPk).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        CartEntity cart = cartRepository.findByUser(user).orElseThrow(() ->
                new CustomException(ErrorCode.CART_NOT_FOUND));

        return UserCartResponseDto.builder().userCartPk(cart.getCart_pk()).build();
    }
}










