package team.themoment.hellogsmassignment.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryOrderResDto {

    private Long orderId;
    private String memberName;
    private String email;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdTime;
    private List<OrderItemDto> orderItems;

}

