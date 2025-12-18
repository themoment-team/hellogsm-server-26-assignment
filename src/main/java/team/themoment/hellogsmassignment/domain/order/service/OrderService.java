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
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public QueryOrderResDto queryOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId)
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
            LocalDate startDate, LocalDate endDate, Pageable pageable
    ) {
        Page<Order> orders = orderRepository.searchOrders(
                status, minPrice, maxPrice, startDate, endDate, pageable);

        SearchOrderInfoDto searchOrderInfoDto = SearchOrderInfoDto.builder()
                .totalPages(orders.getTotalPages())
                .totalElements((int) orders.getTotalElements())
                .build();

        List<SearchOrderResDto> searchOrderResDtos = orders.getContent().stream()
                .map(order -> SearchOrderResDto.builder()
                        .orderId(order.getId())
                        .memberName(order.getMember().getName())
                        .totalPrice(order.getTotalPrice())
                        .status(order.getStatus())
                        .createdTime(order.getCreatedTime())
                        .productCount(order.getOrderItems().size())
                        .build())
                .toList();

        return SearchOrdersResDto.builder()
                .info(searchOrderInfoDto)
                .orders(searchOrderResDtos)
                .build();
    }

}
