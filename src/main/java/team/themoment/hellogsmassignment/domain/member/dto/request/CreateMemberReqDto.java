package team.themoment.hellogsmassignment.domain.member.dto.request;

import java.time.LocalDate;

public record CreateMemberReqDto (
        String name,
        String email,
        String phoneNumber,
        LocalDate birth
) {
}
