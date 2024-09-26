package org.ex.back.domain.order.Service;

import org.ex.back.domain.order.DTO.StoreOrderListResponseDTO;
import org.ex.back.domain.order.Repository.OrderRepository;
import org.ex.back.domain.order.model.OrderEntity;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StoreRepository storeRepository;

    //전체 데이터 조회
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
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
                StoreOrderListResponseDTO storeOrder = StoreOrderListResponseDTO.builder()
                        .orderedAt(order.getOrderedAt())
                        .store_pk(storeDTO)
                        .order_pk(order.getOrder_pk())
                        .isClear(order.getIsClear())
                        .paymentType(order.getPaymentType())
                        .tableNumber(order.getTableNumber())
                        .totalPrice(order.getTotalPrice())
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

    //주문번호 조회
    public Optional<OrderEntity> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    //주문 생성
    public OrderEntity createOrder(OrderEntity order) {
        return orderRepository.save(order);
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
            orderEntity.setPaymentType("환불");
            orderRepository.save(orderEntity);
            return "환불 되었습니다";
        }
        return null;
    }


    //삭제
    public void deleteOrder(String order_pk, Integer store_pk) {
        //storeEntity에서 가져오기
        Optional<StoreEntity> store = storeRepository.findById(store_pk);
        if (store.isEmpty()) {
            return;
        }

        Optional<OrderEntity> order = orderRepository.findById(order_pk);
        if (order.isEmpty()) {
            return;
        }

        orderRepository.delete(order.get());
    }
    //가게별 요일 정산(총금액/카드결제)
    public ResponseEntity<List<Map<String, Object>>> getTotalPriceByDate(Integer store_pk, LocalDate targetDate) {
        Optional<StoreEntity> store = storeRepository.findById(store_pk);

        if (store.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Object[]> results = orderRepository.findTotalPriceByDate(targetDate);
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



}
