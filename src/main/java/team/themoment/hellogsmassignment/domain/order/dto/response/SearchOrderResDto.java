package team.themoment.hellogsmassignment.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrderResDto {
    private Long orderId;
    private String memberName;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdTime;
    private Integer productCount;
}
