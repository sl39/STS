package org.ex.back.domain.order.Controller;


import org.ex.back.domain.order.model.OrderEntity;
import org.ex.back.domain.order.Service.OrderService;
import org.ex.back.domain.order.DTO.StoreOrderListResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
    private OrderService orderService;

    //주문내역 생성
    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity order) {
        OrderEntity createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    //전체 내역 확인용
    @GetMapping
    public List<OrderEntity> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    //주문번호로 조회 DTO사용 손 봐야함.
    @GetMapping("/{orderNum}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable("orderNum") String orderNum) {
        Optional<OrderEntity> order = orderService.getOrderById(orderNum);
        return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 가게별 주문내역 조회 (완료/미완료)
    private ResponseEntity<List<StoreOrderListResponseDTO>> getOrdersByStore(
            Integer store_pk,
            String date,
            boolean isComplete) {

        // 에러처리 코드
        if (store_pk == null || store_pk <= 0) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        LocalDate targetDate;
        // 요청된 날짜가 있을 경우 파싱
        if (date != null && !date.isEmpty()) {
            try {
                targetDate = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
        } else {
            // 요청된 날짜가 없을 경우 오늘 날짜
            targetDate = LocalDate.now();
        }

        List<StoreOrderListResponseDTO> orders = isComplete
                ? orderService.getCompletedOrdersByStore(store_pk)
                : orderService.getIncompleteOrdersByStore(store_pk);

        List<StoreOrderListResponseDTO> filteredOrders = orders.stream()
                .filter(order -> order.getOrderedAt().toLocalDate().equals(targetDate))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredOrders);
    }

    // 가게별 incomplete 주문내역 조회
    @GetMapping("/{store_pk}/incomplete")
    public ResponseEntity<List<StoreOrderListResponseDTO>> getICOrdersByStore(
            @PathVariable("store_pk") Integer store_pk,
            @RequestParam(required = false) String date) {
        return getOrdersByStore(store_pk, date, false);
    }

    // 가게별 complete 주문내역 조회
    @GetMapping("/{store_pk}/complete")
    public ResponseEntity<List<StoreOrderListResponseDTO>> getCOrderByStore(
            @PathVariable("store_pk") Integer store_pk,
            @RequestParam(required = false) String date) {
        return getOrdersByStore(store_pk, date, true);
    }



    //isClear false -> true 수정
    @PutMapping("/{store_pk}/incomplete/{order_pk}/isClear")
    public ResponseEntity<?> updateIsClear(@PathVariable("store_pk") Integer store_pk, @PathVariable("order_pk") String order_pk) {
        boolean isUpdated = orderService.updateIsClear(order_pk, store_pk);
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //환불 처리 수정
    @PutMapping("/{store_pk}/incomplete/{order_pk}/refund")
    public ResponseEntity<?> updatePaymentType(@PathVariable("store_pk") Integer store_pk, @PathVariable("order_pk") String order_pk) {
        String refund = orderService.updatePaymentType(order_pk, store_pk);
        if (refund != null) {
            boolean isUpdated = orderService.updateIsClear(order_pk, store_pk);
            if (isUpdated) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    
    //삭제는 넣어놨는데 과연 필요한 기능인가?
    @DeleteMapping("/{store_pk}/incomplete/{order_pk}/delete")
    public ResponseEntity<Void> deleteOrder(@PathVariable("store_pk") Integer store_pk, @PathVariable("order_pk") String order_pk) {
        orderService.deleteOrder(order_pk, store_pk);
        return ResponseEntity.noContent().build();
    }

    //가게 요일별 정산
    @GetMapping("/{store_pk}/totalPrice")
    public ResponseEntity<List<Map<String, Object>>> getTotalPriceByDate(
            @PathVariable("store_pk") Integer store_pk,
            @RequestParam("date") LocalDate targetDate) {
        return orderService.getTotalPriceByDate(store_pk, targetDate);
    }


    //가게 기간 정산
    @GetMapping("/{store_pk}/totalPriceSelect")
    public ResponseEntity<Map<String, Object>> getTotalPriceByDateRange(
            @PathVariable("store_pk") Integer store_pk,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        return orderService.getTotalPriceByDateRange(store_pk, startDate, endDate);
    }


}

