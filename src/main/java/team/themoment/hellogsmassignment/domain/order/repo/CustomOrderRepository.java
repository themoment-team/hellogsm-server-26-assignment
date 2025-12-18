package team.themoment.hellogsmassignment.domain.order.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import team.themoment.hellogsmassignment.domain.order.entity.Order;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CustomOrderRepository {
    Optional<Order> findByOrderId(Long orderId);

    Page<Order> searchOrders(OrderStatus status,
                             BigDecimal minPrice,
                             BigDecimal maxPrice,
                             LocalDate startDate,
                             LocalDate endDate,
                             Pageable pageable
    );
}
