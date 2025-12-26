package team.themoment.hellogsmassignment.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.themoment.hellogsmassignment.domain.member.dto.response.FoundMemberResDto;
import team.themoment.hellogsmassignment.domain.member.entity.Member;
import team.themoment.hellogsmassignment.domain.member.entity.type.AuthReferrerType;
import team.themoment.hellogsmassignment.domain.member.repo.MemberRepository;
import team.themoment.hellogsmassignment.domain.member.service.FoundMemberService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member 조회 Service 클래스의")
public class FoundMemberServiceTest {
    @InjectMocks
    private FoundMemberService foundMemberService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {
        @Nested
        @DisplayName("Member ID가 주어졌을 때")
        class Context_with_memberId {
            private Member existMember;

            @BeforeEach
            void setUp() {
                existMember = Member.builder()
                                .id(1L)
                                .name("홍길동")
                                .email("test@example.com")
                                .authReferrerType(AuthReferrerType.GOOGLE)
                                .phoneNumber("010-0000-0000")
                                .birth(LocalDate.of(2000,1,1))
                                .build();
                given(memberRepository.findById(1L)).willReturn(
                        Optional.of(existMember)
                );
            }

            @Test
            @DisplayName("Member를 조회하여 적절한 ResDto를 반환한다.")
            void it_save_member() {
                // when
                FoundMemberResDto result = foundMemberService.execute(1L);

                // then
                assertThat(result).isNotNull();
                assertThat(result.memberId()).isEqualTo(1L);
                assertThat(result.name()).isEqualTo("홍길동");
                assertThat(result.phoneNumber()).isEqualTo("010-0000-0000");
                assertThat(result.birth()).isEqualTo(LocalDate.of(2000,1,1));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 Member ID가 주어졌을 때")
        class Context_with_not_exist_memberId {
            @BeforeEach
            void setUp() {
                given(memberRepository.findById(1L)).willReturn(Optional.empty());
            }

            @Test
            @DisplayName("Member ID 찾을 수 없음 예외를 던진다.")
            void it_throw_memberId_exception() {
                // when & then
                assertThatThrownBy(() -> foundMemberService.execute(1L))
                        .isInstanceOf(RuntimeException.class);

                verify(memberRepository, never()).save(any(Member.class));
            }
        }
    }
}

