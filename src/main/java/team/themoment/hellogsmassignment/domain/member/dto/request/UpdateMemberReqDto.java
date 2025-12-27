package team.themoment.hellogsmassignment.domain.member.dto.request;

import java.time.LocalDate;

public record UpdateMemberReqDto (
        String name,
        String email,
        String phoneNumber,
        LocalDate birth
) {
}
