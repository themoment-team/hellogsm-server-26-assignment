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
@DisplayName("UpdateMemberService 클래스의")
class UpdateMemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UpdateMemberService updateMemberService;

    private Long memberId;
    private Member member;
    private UpdateMemberReqDto reqDto;

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

    @BeforeEach
    void setUp() {
        reqDto = createUpdateDto();
    }

    @Nested
    class execute_메서드는 {

        @Nested
        @DisplayName("Member 업데이트 DTO가 주어졌을 때")
        class when_update_member_dto_is_given {

            @BeforeEach
            void setUp() {
                memberId = 1L;
                member = createMember(memberId);

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(false);
                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.of(member));
            }

            @Test
            @DisplayName("DTO 정보에 따라 Member 정보를 업데이트하고 저장한다")
            void update_member_success() {
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

        @Nested
        @DisplayName("존재하지 않는 Member ID가 주어졌을 때")
        class when_member_id_does_not_exist {

            @BeforeEach
            void setUp() {
                memberId = 999L;

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(false);
                when(memberRepository.findById(memberId))
                        .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("Member ID를 찾을 수 없다는 예외를 던진다")
            void update_member_fail_when_member_not_found() {
                // when & then
                assertThrows(RuntimeException.class,
                        () -> updateMemberService.execute(memberId, reqDto));

                verify(memberRepository).findById(memberId);
                verify(memberRepository, never()).save(any());
            }
        }

        @Nested
        @DisplayName("중복된 Email이 주어졌을 때")
        class when_duplicate_email_is_given {

            @BeforeEach
            void setUp() {
                memberId = 1L;

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(true);
            }

            @Test
            @DisplayName("Email 중복 예외를 던진다")
            void update_member_fail_when_email_duplicated() {
                // when & then
                assertThrows(RuntimeException.class,
                        () -> updateMemberService.execute(memberId, reqDto));

                verify(memberRepository).existsByEmail(reqDto.getEmail());
                verify(memberRepository, never()).existsByPhoneNumber(any());
                verify(memberRepository, never()).findById(any());
                verify(memberRepository, never()).save(any());
            }
        }

        @Nested
        @DisplayName("중복된 PhoneNumber가 주어졌을 때")
        class when_duplicate_phone_number_is_given {

            @BeforeEach
            void setUp() {
                memberId = 1L;

                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(true);
            }

            @Test
            @DisplayName("PhoneNumber 중복 예외를 던진다")
            void update_member_fail_when_phone_number_duplicated() {
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
