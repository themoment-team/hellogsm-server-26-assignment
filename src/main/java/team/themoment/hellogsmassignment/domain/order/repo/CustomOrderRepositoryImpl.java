package team.themoment.hellogsmassignment.domain.order.repo;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team.themoment.hellogsmassignment.domain.order.entity.Order;
import team.themoment.hellogsmassignment.domain.order.entity.QOrder;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    private final JPAQueryFactory queryFactory;

    private static final QOrder order = QOrder.order;

    @Override
    public Optional<Order> findByOrderId(Long orderId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(order)
                        .leftJoin(order.orderItems).fetchJoin()
                        .leftJoin(order.member).fetchJoin()
                        .where(order.id.eq(orderId))
                        .distinct()
                        .fetchOne()
        );
    }

    @Override
    public Page<Order> searchOrders(OrderStatus status,
                                    BigDecimal minPrice,
                                    BigDecimal maxPrice,
                                    LocalDate startDate,
                                    LocalDate endDate,
                                    Pageable pageable
    ) {
        List<Long> orderIds = queryFactory
                .select(order.id)
                .from(order)
                .where(
                        statusEq(status),
                        minPriceGoe(minPrice),
                        maxPriceLoe(maxPrice),
                        startDateGoe(startDate),
                        endDateLoe(endDate)
                )
                .orderBy(order.createdTime.desc(), order.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        statusEq(status),
                        minPriceGoe(minPrice),
                        maxPriceLoe(maxPrice),
                        startDateGoe(startDate),
                        endDateLoe(endDate)
                )
                .fetchOne();

        long totalCount = total != null ? total : 0L;

        if (orderIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, totalCount);
        }

        List<Order> content = queryFactory
                .select(order).distinct()
                .from(order)
                .leftJoin(order.orderItems).fetchJoin()
                .leftJoin(order.member).fetchJoin()
                .where(order.id.in(orderIds))
                .orderBy(order.createdTime.desc(), order.id.desc())
                .fetch();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression statusEq(OrderStatus status) {
        return status != null ? order.status.eq(status) : null;
    }

    private BooleanExpression minPriceGoe(BigDecimal minPrice) {
        return minPrice != null ? order.totalPrice.goe(minPrice) : null;
    }

    private BooleanExpression maxPriceLoe(BigDecimal maxPrice) {
        return maxPrice != null ? order.totalPrice.loe(maxPrice) : null;
    }

    private BooleanExpression startDateGoe(LocalDate startDate) {
        return startDate != null ? order.createdTime.goe(startDate.atStartOfDay()) : null;
    }

    private BooleanExpression endDateLoe(LocalDate endDate) {
        return endDate != null ? order.createdTime.loe(endDate.atTime(23, 59, 59)) : null;
    }
}
