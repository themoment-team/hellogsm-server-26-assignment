package team.themoment.hellogsmassignment.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrderInfoDto {
    private Integer totalPages;
    private Integer totalElements;
}
