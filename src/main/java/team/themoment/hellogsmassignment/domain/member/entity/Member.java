package team.themoment.hellogsmassignment.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import team.themoment.hellogsmassignment.domain.member.dto.request.UpdateMemberReqDto;
import team.themoment.hellogsmassignment.domain.member.entity.type.AuthReferrerType;
import team.themoment.hellogsmassignment.domain.member.entity.type.Role;
import team.themoment.hellogsmassignment.domain.member.entity.type.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "tb_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_referrer_type", nullable = false)
    private AuthReferrerType authReferrerType;

    @Column(name = "name")
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @CreatedDate
    @Column(name = "created_time", updatable = false, nullable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    public void updateMember(UpdateMemberReqDto reqDto) {
        this.email = reqDto.email();
        this.name = reqDto.name();
        this.birth = reqDto.birth();
        this.phoneNumber = reqDto.phoneNumber();
    }

}
