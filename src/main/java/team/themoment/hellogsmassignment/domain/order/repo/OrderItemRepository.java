package team.themoment.hellogsmassignment.domain.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import team.themoment.hellogsmassignment.domain.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
