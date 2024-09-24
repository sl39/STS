package org.ex.back.domain.order.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ex.back.domain.store.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

	//store_pk로 현재 주문내역 리스트
	List<OrderEntity> findAllByStoreAndIsClearFalse(StoreEntity store_pk);

	//store_pk로 완료된 주문내역 리스트
	List<OrderEntity> findAllByStoreAndIsClearTrue(StoreEntity store_pk);


	//가게 요일별 정산 쿼리문(총금액/카드결제)
	@Query("SELECT DATE(o.orderedAt), " +
			"SUM(o.totalPrice) - SUM(CASE WHEN o.paymentType = '환불' THEN o.totalPrice ELSE 0 END) AS totalPriceAfterRefund, " +
			"SUM(o.totalPrice) - SUM(CASE WHEN o.paymentType = '환불' THEN o.totalPrice ELSE 0 END) - SUM(CASE WHEN o.paymentType = '현장결제' THEN o.totalPrice ELSE 0 END) AS totalPriceAfterOnSitePayment " +
			"FROM OrderEntity o WHERE o.isClear = true AND DATE(o.orderedAt) = :targetDate " +
			"GROUP BY DATE(o.orderedAt)")
	List<Object[]> findTotalPriceByDate(@Param("targetDate") LocalDate targetDate);


	//가게 기간 정산 쿼리문(총금액/카드결제)
	@Query("SELECT DATE(o.orderedAt), " +
			"SUM(o.totalPrice) - SUM(CASE WHEN o.paymentType = '환불' THEN o.totalPrice ELSE 0 END) AS totalPriceAfterRefund, " +
			"SUM(o.totalPrice) - SUM(CASE WHEN o.paymentType IN ('환불', '현장결제') THEN o.totalPrice ELSE 0 END) AS totalPriceAfterOnSitePayment " +
			"FROM OrderEntity o WHERE o.isClear = true AND o.orderedAt BETWEEN :startDate AND :endDate " +
			"GROUP BY DATE(o.orderedAt)")
	List<Object[]> findTotalPriceByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
