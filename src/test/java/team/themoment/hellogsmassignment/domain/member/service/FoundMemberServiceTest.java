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
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Member_조회_Service_클래스의 {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FoundMemberService foundMemberService;

    private Member createMember(Long id) {
        return Member.builder()
                .id(id)
                .name("홍길은")
                .email("test@test.com")
                .birth(LocalDate.of(2000, 1, 1))
                .phoneNumber("01012345678")
                .build();
    }
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @Nested
    class execute_메서드는 {

        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        @Nested
        class Member_ID가_주어졌을_때 {

            @Test
            @DisplayName("Member를 조회하여 적절한 ResDTO를 반환한다")
            void findMember_success() {
                // given
                Long memberId = 1L;
                Member member = createMember(memberId);

                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.of(member));

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

        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        @Nested
        class 존재하지_않는_Member_ID가_주어졌을_때{

            @Test
            @DisplayName("Member ID 찾을 수 없음 예외를 던진다")
            void findMember_fail_whenMemberNotFound() {
                // given
                Long memberId = 999L;

                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.empty());

                // when & then
                assertThrows(RuntimeException.class,
                        () -> foundMemberService.execute(memberId));
                verify(memberRepository).findById(memberId);
            }
        }
    }
}
