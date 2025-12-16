package team.themoment.hellogsmassignment.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.themoment.hellogsmassignment.domain.order.dto.response.*;
import team.themoment.hellogsmassignment.domain.order.entity.Order;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;
import team.themoment.hellogsmassignment.domain.order.repo.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public QueryOrderResDto queryOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(oi -> OrderItemDto.builder()
                    .orderItemId(oi.getId())
                    .price(oi.getPrice())
                    .quantity(oi.getQuantity())
                    .productName(oi.getProduct().getName())
                    .build()
                ).toList();

        return QueryOrderResDto.builder()
                .orderId(orderId)
                .memberName(order.getMember().getName())
                .email(order.getMember().getEmail())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdTime(order.getCreatedTime())
                .orderItems(orderItemDtos)
                .build();
    }

    @Transactional(readOnly = true)
    public SearchOrdersResDto searchOrders(
            OrderStatus status, BigDecimal minPrice, BigDecimal maxPrice,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable
    ) {
        Page<SearchOrderResDto> page =
                orderRepository.searchOrders(
                        status, minPrice, maxPrice,
                        startDate, endDate, pageable
                );

        SearchOrderInfoDto info = SearchOrderInfoDto.builder()
                .totalPages(page.getTotalPages())
                .totalElements((int) page.getTotalElements())
                .build();

        return SearchOrdersResDto.builder()
                .info(info)
                .orders(page.getContent())
                .build();
    }

}
