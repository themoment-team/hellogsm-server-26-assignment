package team.themoment.hellogsmassignment.domain.member.service;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.themoment.hellogsmassignment.domain.member.dto.response.FoundMemberResDto;
import team.themoment.hellogsmassignment.domain.member.entity.Member;
import team.themoment.hellogsmassignment.domain.member.repo.MemberRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FoundMemberService 클래스의")
class FoundMemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FoundMemberService foundMemberService;

    private Long memberId;
    private Member member;

    private Member createMember(Long id) {
        return Member.builder()
                .id(id)
                .name("홍길은")
                .email("test@test.com")
                .birth(LocalDate.of(2000, 1, 1))
                .phoneNumber("01012345678")
                .build();
    }

    @Nested
    class execute_메서드는 {

        @Nested
        @DisplayName("Member ID가 주어졌을 때")
        class when_member_id_is_given {

            @BeforeEach
            void setUp() {
                memberId = 1L;
                member = createMember(memberId);

                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.of(member));
            }

            @Test
            @DisplayName("Member를 조회하여 적절한 ResDTO를 반환한다")
            void find_member_success() {
                // when
                FoundMemberResDto result = foundMemberService.execute(memberId);

                // then
                verify(memberRepository).findById(memberId);

                assertEquals(memberId, result.memberId());
                assertEquals(member.getName(), result.name());
                assertEquals(member.getPhoneNumber(), result.phoneNumber());
                assertEquals(member.getBirth(), result.birth());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 Member ID가 주어졌을 때")
        class when_member_id_does_not_exist {

            @BeforeEach
            void setUp() {
                memberId = 999L;

                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("Member ID 찾을 수 없음 예외를 던진다")
            void find_member_fail_when_member_not_found() {
                // when & then
                assertThrows(RuntimeException.class,
                        () -> foundMemberService.execute(memberId));

                verify(memberRepository).findById(memberId);
            }
        }
    }
}
