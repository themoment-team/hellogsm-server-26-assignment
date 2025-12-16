package team.themoment.hellogsmassignment.domain.order.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.themoment.hellogsmassignment.domain.order.dto.response.SearchOrderResDto;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderRepositoryCustom {
    Page<SearchOrderResDto> searchOrders(
            OrderStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}
