package team.themoment.hellogsmassignment.domain.member.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.themoment.hellogsmassignment.domain.member.dto.request.UpdateMemberReqDto;
import team.themoment.hellogsmassignment.domain.member.entity.Member;
import team.themoment.hellogsmassignment.domain.member.repo.MemberRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Member_업데이트_Service_클래스의 {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UpdateMemberService updateMemberService;

    private Member createMember(Long id) {
        return Member.builder()
                .id(id)
                .name("홍길금")
                .email("old@test.com")
                .phoneNumber("01000000000")
                .birth(LocalDate.of(1999, 1, 1))
                .build();
    }

    private UpdateMemberReqDto createUpdateDto() {
        return new UpdateMemberReqDto(
                "변경이름",
                "new@test.com",
                "01012345678",
                LocalDate.of(2000, 1, 1)
        );
    }

    @Nested
    class execute_메서드는 {

        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        @Nested
        class Member_업데이트_DTO가_주어졌을_때 {

            @Test
            @DisplayName("DTO 정보에 따라 Member 정보를 업데이트하고 저장한다")
            void updateMember_success() {
                // given
                Long memberId = 1L;
                Member member = createMember(memberId);
                UpdateMemberReqDto reqDto = createUpdateDto();

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(false);
                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.of(member));

                // when
                updateMemberService.execute(memberId, reqDto);

                // then (Interaction 검증)
                verify(memberRepository).existsByEmail(reqDto.getEmail());
                verify(memberRepository).existsByPhoneNumber(reqDto.getPhoneNumber());
                verify(memberRepository).findById(memberId);
                verify(memberRepository).save(argThat(savedMember ->
                        savedMember.getId().equals(memberId) &&
                                savedMember.getName().equals(reqDto.getName()) &&
                                savedMember.getEmail().equals(reqDto.getEmail()) &&
                                savedMember.getPhoneNumber().equals(reqDto.getPhoneNumber()) &&
                                savedMember.getBirth().equals(reqDto.getBirth())
                ));
            }
        }

        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        @Nested
        class 존재하지_않는_Member_ID가_주어졌을_때 {

            @Test
            @DisplayName("Member ID를 찾을 수 없다는 예외를 던진다")
            void updateMember_fail_whenMemberNotFound() {
                // given
                Long memberId = 999L;
                UpdateMemberReqDto reqDto = createUpdateDto();

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(false);
                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.empty());

                // when & then
                assertThrows(RuntimeException.class,
                        () -> updateMemberService.execute(memberId, reqDto));

                verify(memberRepository).findById(memberId);
                verify(memberRepository, never()).save(any());
            }
        }

        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        @Nested
        class 중복된_Email이_주어졌을_때 {

            @Test
            @DisplayName("Email 중복 예외를 던진다")
            void updateMember_fail_whenEmailDuplicated() {
                // given
                Long memberId = 1L;
                UpdateMemberReqDto reqDto = createUpdateDto();

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(true);

                // when & then
                assertThrows(RuntimeException.class,
                        () -> updateMemberService.execute(memberId, reqDto));

                verify(memberRepository).existsByEmail(reqDto.getEmail());
                verify(memberRepository, never()).existsByPhoneNumber(any());
                verify(memberRepository, never()).findById(any());
                verify(memberRepository, never()).save(any());
            }
        }

        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        @Nested
        class 중복된_PhoneNumber가_주어졌을_때 {

            @Test
            @DisplayName("PhoneNumber 중복 예외를 던진다")
            void updateMember_fail_whenPhoneNumberDuplicated() {
                // given
                Long memberId = 1L;
                UpdateMemberReqDto reqDto = createUpdateDto();

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(true);

                // when & then
                assertThrows(RuntimeException.class,
                        () -> updateMemberService.execute(memberId, reqDto));

                verify(memberRepository).existsByEmail(reqDto.getEmail());
                verify(memberRepository).existsByPhoneNumber(reqDto.getPhoneNumber());
                verify(memberRepository, never()).findById(any());
                verify(memberRepository, never()).save(any());
            }
        }
    }
}
