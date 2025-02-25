package team.themoment.hellogsmassignment.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.themoment.hellogsmassignment.domain.order.dto.response.QueryOrderResDto;
import team.themoment.hellogsmassignment.domain.order.dto.response.SearchOrdersResDto;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;
import team.themoment.hellogsmassignment.domain.order.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{order_id}")
    public ResponseEntity<QueryOrderResDto> getOrderById(
            @PathVariable("order_id") Long orderId
    ) {
        QueryOrderResDto response = orderService.queryOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchOrdersResDto> searchOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        SearchOrdersResDto response = orderService.searchOrders(status, minPrice, maxPrice, startDate, endDate, pageable);
        return ResponseEntity.ok(response);
    }
}
