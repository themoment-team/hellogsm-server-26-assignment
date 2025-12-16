package team.themoment.hellogsmassignment.domain.order.repo;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team.themoment.hellogsmassignment.domain.member.entity.QMember;
import team.themoment.hellogsmassignment.domain.order.dto.response.SearchOrderResDto;
import team.themoment.hellogsmassignment.domain.order.entity.QOrder;
import team.themoment.hellogsmassignment.domain.order.entity.QOrderItem;
import team.themoment.hellogsmassignment.domain.order.entity.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    private final QOrder order = QOrder.order;
    private final QMember member = QMember.member;
    private final QOrderItem orderItem = QOrderItem.orderItem;

    @Override
    public Page<SearchOrderResDto> searchOrders(
            OrderStatus status, BigDecimal minPrice, BigDecimal maxPrice,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable
    ) {
        List<SearchOrderResDto> content = queryFactory
                .select(Projections.constructor(
                        SearchOrderResDto.class,
                        order.id,
                        member.name,
                        order.totalPrice,
                        order.status,
                        order.createdTime,
                        orderItem.id.count().intValue()
                ))
                .from(order)
                .join(order.member, member)
                .leftJoin(order.orderItems, orderItem)
                .where(
                        statusEq(status),
                        minPriceGoe(minPrice),
                        maxPriceLoe(maxPrice),
                        createdBetween(startDate, endDate)
                )
                .groupBy(order.id, member.name)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total =
                queryFactory
                        .select(order.count())
                        .from(order)
                        .where(
                                statusEq(status),
                                minPriceGoe(minPrice),
                                maxPriceLoe(maxPrice),
                                createdBetween(startDate, endDate)
                        )
                        .fetchOne();

        return new PageImpl<>(content, pageable, total);
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

    private BooleanExpression startDateGoe(LocalDateTime startDate) {
        return startDate != null ? order.createdTime.goe(startDate) : null;
    }

    private BooleanExpression endDateLoe(LocalDateTime endDate) {
        return endDate != null ? order.createdTime.loe(endDate) : null;
    }

    private BooleanExpression createdBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        if (startDate == null && endDate == null) {
            return null;
        }

        if (startDate != null && endDate != null) {
            return QOrder.order.createdTime.between(startDate, endDate);
        }

        if (startDate != null) {
            return QOrder.order.createdTime.goe(startDate);
        }

        return QOrder.order.createdTime.loe(endDate);
    }
}
