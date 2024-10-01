package org.ex.back.domain.order.Service;

import org.ex.back.domain.cart.model.CartEntity;
import org.ex.back.domain.cart.model.CartItemEntity;
import org.ex.back.domain.cart.repository.CartRepository;
import org.ex.back.domain.order.DTO.*;
import org.ex.back.domain.order.Repository.OrderRepository;
import org.ex.back.domain.order.model.OrderEntity;
import org.ex.back.domain.order.model.OrderItemEntity;
import org.ex.back.domain.sms.Service.KakaoService;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.repository.StoreRepository;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KakaoService kakaoService;

    //전체 데이터 조회 - 확인용
//    public List<OrderEntity> getAllOrders() {
//        return orderRepository.findAll();
//    }

    //주문 생성
    public OrderEntity createOrder(String order_pk,Integer cart_pk, Integer user_pk, Integer store_pk, String paymentType, String payNumber) {
        CartEntity cartEntity = cartRepository.findById(cart_pk).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItemEntity> cartItems = cartEntity.getCartItems();

        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (CartItemEntity cartItem : cartItems) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setMenu(cartItem.getMenu());
            orderItem.setOptionItemList(cartItem.getOptionItemList());
            orderItem.setTotalExtraPrice(cartItem.getTotalExtraPrice());
            orderItem.setMenuCount(cartItem.getMenuCount());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItems.add(orderItem);

        }
        UserEntity userEntity = userRepository.findById(user_pk).orElseThrow(() -> new RuntimeException("Cart not found"));
        StoreEntity storeEntity = storeRepository.findById(store_pk).orElseThrow(() -> new RuntimeException("Cart not found"));


        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrder_pk(order_pk);
        orderEntity.setOrderItems(orderItems);
        orderEntity.setUser(userEntity);
        orderEntity.setStore(storeEntity);
        orderEntity.setPaymentType(paymentType);
        orderEntity.setPayNumber(payNumber);
        orderEntity.setTableNumber(cartEntity.getTableNumber());
        orderEntity.setTotalPrice(cartEntity.getTotalPrice());
        orderEntity.setGuestPhone(userEntity.getPhone());
        orderEntity.setIsClear(false);
        orderEntity.setOrderedAt(LocalDateTime.now());

        //알림톡 전송 부분 비활성화 - 작동 가능
//        kakaoService.sendOrderKakaoMessage(
//                orderEntity.getGuestPhone(),
//                orderEntity.getOrder_pk(),
//                orderEntity.getTotalPrice(),
//                orderEntity.getOrderedAt());

        return orderRepository.save(orderEntity);
    }
    // fcm 데이터 양식
    public StoreOrderListResponseDTO getOrder(OrderEntity createdOrder) {

                List<OrderItemCheckDTO> orderItems = createdOrder.getOrderItems().stream()
                        .map(item -> new OrderItemCheckDTO(
                                item.getMenu().getName(),
                                item.getMenuCount(),
                                item.getOptionItemList()
                        )).collect(Collectors.toList());

                StoreOrderListResponseDTO storeOrder = StoreOrderListResponseDTO.builder()
                        .orderedAt(createdOrder.getOrderedAt())
                        .store_pk(createdOrder.getStore().getStore_pk())
                        .order_pk(createdOrder.getOrder_pk())
                        .paymentType(createdOrder.getPaymentType())
                        .tableNumber(createdOrder.getTableNumber())
                        .totalPrice(createdOrder.getTotalPrice())
                        .orderItems(orderItems)
                        .build();
                return storeOrder;
    }

    // 가게 주문검색 공통부분 DTO 사용부분
    public List<StoreOrderListResponseDTO> getOrdersByStore(Integer storeDTO, boolean isClear) {
        Optional<StoreEntity> store = storeRepository.findById(storeDTO);
        if (store.isPresent()) {
            List<OrderEntity> orderList = isClear
                    ? orderRepository.findAllByStoreAndIsClearTrue(store.get())
                    : orderRepository.findAllByStoreAndIsClearFalse(store.get());

            List<StoreOrderListResponseDTO> storeOrderListResponseDTOList = new ArrayList<>();
            for (OrderEntity order : orderList) {
                List<OrderItemCheckDTO> orderItems = order.getOrderItems().stream()
                        .map(item -> new OrderItemCheckDTO(
                                item.getMenu().getName(),
                                item.getMenuCount(),
                                item.getOptionItemList()
                        )).collect(Collectors.toList());

                StoreOrderListResponseDTO storeOrder = StoreOrderListResponseDTO.builder()
                        .orderedAt(order.getOrderedAt())
                        .store_pk(storeDTO)
                        .order_pk(order.getOrder_pk())
                        .paymentType(order.getPaymentType())
                        .tableNumber(order.getTableNumber())
                        .totalPrice(order.getTotalPrice())
                        .orderItems(orderItems)
                        .build();
                storeOrderListResponseDTOList.add(storeOrder);
            }

            return storeOrderListResponseDTOList;
        } else {
            return Collections.emptyList();
        }
    }
    // 미완료 주문 검색
    public List<StoreOrderListResponseDTO> getIncompleteOrdersByStore(Integer storeDTO) {
        return getOrdersByStore(storeDTO, false);
    }
    // 완료된 주문 검색
    public List<StoreOrderListResponseDTO> getCompletedOrdersByStore(Integer storeDTO) {
        return getOrdersByStore(storeDTO, true);
    }


    //주문번호 기반 조회
    public OrderNumberDTO getOrderById(String order_pk) {
        Optional<OrderEntity> orderId = orderRepository.findById(order_pk);

        if (orderId.isPresent()){
            OrderEntity order = orderId.get();

            List<OrderItemTotalDTO> orderItems = order.getOrderItems().stream()
                    .map(item -> new OrderItemTotalDTO(
                            item.getMenu().getName(),
                            item.getMenuCount(),
                            item.getOptionItemList(),
                            item.getTotalPrice(),
                            item.getTotalExtraPrice()
                    )).collect(Collectors.toList());

            return OrderNumberDTO.builder()
                    .order_pk(order.getOrder_pk())
                    .orderedAt(order.getOrderedAt())
                    .orderItems(orderItems)
                    .totalPrice(order.getTotalPrice())
                    .paymentType(order.getPaymentType())
                    .build();
        } else {
            return null;
        }
    }

    //사용자 simple조회
    public List<OrderUserSimpleDTO> getOrderByUserPkSimple(Integer userPk) {
        UserEntity user = userRepository.findById(userPk)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        List<OrderEntity> orders = orderRepository.findByUser(user);

        // OrderEntity 리스트를 OrderUserSimpleDTO 리스트로 변환하여 반환
        return orders.stream()
                .map(order -> OrderUserSimpleDTO.builder()
                        .order_pk(order.getOrder_pk())
                        .user_pk(order.getUser().getUser_pk())
                        .storeName(order.getStore().getStoreName())
                        .storeAddress(order.getStore().getAddress())
                        .orderedAt(order.getOrderedAt())
                        .storeImageUrl(order.getStore().getStoreImages().isEmpty() ? null : order.getStore().getStoreImages().get(0).getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
    //사용자 detail조회
    public OrderUserDetailDTO getOrderByUserPkDetail(String order_pk) {
        Optional<OrderEntity> orderId = orderRepository.findById(order_pk);

        if (orderId.isPresent()){
            OrderEntity order = orderId.get();

            List<OrderItemCheckDTO> orderItems = order.getOrderItems().stream()
                    .map(item -> new OrderItemCheckDTO(
                            item.getMenu().getName(),
                            item.getMenuCount(),
                            item.getOptionItemList()
                    )).collect(Collectors.toList());

            return OrderUserDetailDTO.builder()
                    .order_pk(order.getOrder_pk())
                    .storeName(order.getStore().getStoreName())
                    .storeAddress(order.getStore().getAddress())
                    .totalPrice(order.getTotalPrice())
                    .orderedAt(order.getOrderedAt())
                    .paymentType(order.getPaymentType())
                    .orderItems(orderItems)
                    .build();
        } else {
            return null;
        }
    }


    //isClear true로 수정
    public boolean updateIsClear(String order_pk, Integer store_pk) {
        Optional<StoreEntity> store = storeRepository.findById(store_pk);
        if (store.isEmpty()){
            return false;
        }
        Optional<OrderEntity> order = orderRepository.findById(order_pk);
        if (order.isPresent()) {
            OrderEntity orderEntity = order.get();
            orderEntity.setIsClear(true);
            orderRepository.save(orderEntity);
            return true;
        }
        return false;
    }

    //환불처리 수정
    public String updatePaymentType(String order_pk, Integer store_pk) {
        Optional<StoreEntity> store = storeRepository.findById(store_pk);
        if (store.isEmpty()){
            return null;
        }
        Optional<OrderEntity> order = orderRepository.findById(order_pk);
        if (order.isPresent()) {
            OrderEntity orderEntity = order.get();

            //POST 요청 준비 - 결제취소 연결
            String payNumber = orderEntity.getPayNumber();
            String url = "http://192.168.30.16:3000/api/cancel-payment";
            //Json 데이터 생성
            Map<String, String> requestData = new HashMap<>();
            requestData.put("merchant_uid", order_pk);
            requestData.put("imp_uid ", payNumber);

            //헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            //요청 엔티티 생성
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData, headers);
            //post 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            try {
                String response = restTemplate.postForObject(url, requestEntity, String.class);
                //isClear, paymentType 수정
                if(response != null){
                    orderEntity.setPaymentType("환불");
                    orderEntity.setIsClear(true);
                    orderRepository.save(orderEntity);
                    return "환불 되었습니다";
                }
            } catch (Exception e) {
                return "결제 취소 실패하였습니다";
            }
        }
        return null;
    }

    //삭제
//    public void deleteOrder(String order_pk, Integer store_pk) {
//        //storeEntity에서 가져오기
//        Optional<StoreEntity> store = storeRepository.findById(store_pk);
//        if (store.isEmpty()) {
//            return;
//        }
//
//        Optional<OrderEntity> order = orderRepository.findById(order_pk);
//        if (order.isEmpty()) {
//            return;
//        }
//
//        orderRepository.delete(order.get());
//    }

    //가게별 요일 정산(총금액/카드결제)
    public ResponseEntity<List<Map<String, Object>>> getTotalPriceByMonth(Integer store_pk, String targetMonth) {
        Optional<StoreEntity> store = storeRepository.findById(store_pk);

        if (store.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Object[]> results = orderRepository.findTotalPriceByMonth(targetMonth);
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Long totalPriceAfterRefund = ((Number) result[1]).longValue();
            Long totalPriceAfterOnSitePayment = ((Number) result[2]).longValue();

            Map<String, Object> priceDetails = new HashMap<>();
            priceDetails.put("date", date.toString());
            priceDetails.put("totalPrice", totalPriceAfterRefund);
            priceDetails.put("card", totalPriceAfterOnSitePayment);

            responseList.add(priceDetails);
        }

        return ResponseEntity.ok(responseList);
    }


    //가게별 기간 정산(총금액/카드결제)
    public ResponseEntity<Map<String, Object>> getTotalPriceByDateRange(Integer store_pk, LocalDate startDate, LocalDate endDate) {
        Optional<StoreEntity> store = storeRepository.findById(store_pk);

        if (store.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        //tiem 시작점과 끝점 디폴트 처리
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 시작점: 자정
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 종료점: 23:59:59

        List<Object[]> results = orderRepository.findTotalPriceByDateRange(startDateTime, endDateTime);

        long totalPriceSum = 0;
        long cardSum = 0;

        for (Object[] result : results) {
            totalPriceSum += ((Number) result[1]).longValue();
            cardSum += ((Number) result[2]).longValue();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("totalPrice", totalPriceSum);
        response.put("card", cardSum);

        return ResponseEntity.ok(response);
    }
    // fcm 토큰 저장
    public void saveToken(Integer storePk, String token) {
        StoreEntity storeEntity = storeRepository.findById(storePk)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        storeEntity.setFcmToken(token);

        storeRepository.save(storeEntity);
    }


}
