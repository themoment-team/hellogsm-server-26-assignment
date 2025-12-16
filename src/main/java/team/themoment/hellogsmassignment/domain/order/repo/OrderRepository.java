package team.themoment.hellogsmassignment.domain.order.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.themoment.hellogsmassignment.domain.order.entity.Order;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

//    @Query("SELECT o FROM Order o " +
//            "WHERE (:status IS NULL OR o.status = :status) " +
//            "AND (:minPrice IS NULL OR o.totalPrice >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR o.totalPrice <= :maxPrice) " +
//            "AND (:startDate IS NULL OR o.createdTime >= :startDate) " +
//            "AND (:endDate IS NULL OR o.createdTime <= :endDate)")
//    Page<Order> searchOrders(@Param("status") OrderStatus status,
//                             @Param("minPrice") BigDecimal minPrice,
//                             @Param("maxPrice") BigDecimal maxPrice,
//                             @Param("startDate") LocalDateTime startDate,
//                             @Param("endDate") LocalDateTime endDate,
//                             Pageable pageable);
//
//    @Query("SELECT count(*) FROM Order o " +
//            "WHERE (:status IS NULL OR o.status = :status) " +
//            "AND (:minPrice IS NULL OR o.totalPrice >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR o.totalPrice <= :maxPrice) " +
//            "AND (:startDate IS NULL OR o.createdTime >= :startDate) " +
//            "AND (:endDate IS NULL OR o.createdTime <= :endDate)")
//    int countSearchOrder(@Param("status") OrderStatus status,
//                             @Param("minPrice") BigDecimal minPrice,
//                             @Param("maxPrice") BigDecimal maxPrice,
//                             @Param("startDate") LocalDateTime startDate,
//                             @Param("endDate") LocalDateTime endDate);

}
