package team.themoment.hellogsmassignment.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.themoment.hellogsmassignment.domain.member.dto.request.UpdateMemberReqDto;
import team.themoment.hellogsmassignment.domain.member.entity.Member;
import team.themoment.hellogsmassignment.domain.member.entity.type.AuthReferrerType;
import team.themoment.hellogsmassignment.domain.member.repo.MemberRepository;
import team.themoment.hellogsmassignment.domain.member.service.UpdateMemberService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member 업데이트 Service 클래스의")
public class UpdateMemberServiceTest {
    @InjectMocks
    private UpdateMemberService updateMemberService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {
        @Nested
        @DisplayName("Member 업데이트 DTO 객체가 주어졌을때")
        class Context_with_valid_dto {
            private UpdateMemberReqDto reqDto;

            @BeforeEach
            void setUp() {
                reqDto = new UpdateMemberReqDto(
                        "홍길동",
                        "test1@example.com",
                        "010-1234-5678",
                        LocalDate.of(2000, 1, 1)
                );

                Optional<Member> existMember = Optional.ofNullable(
                        Member.builder()
                                .id(1L)
                                .email("test@example.com")
                                .authReferrerType(AuthReferrerType.GOOGLE)
                                .phoneNumber("010-0000-0000")
                                .build()
                );
                given(memberRepository.findById(1L)).willReturn(existMember);
                given(memberRepository.existsByEmail(reqDto.email())).willReturn(false);
                given(memberRepository.existsByPhoneNumber(reqDto.phoneNumber())).willReturn(false);
            }

            @Test
            @DisplayName("DTO 객체의 정보에 따라 Member의 정보를 업데이트하여 save 한다.")
            void it_save_member() {
                // when
                updateMemberService.execute(1L, reqDto);

                // then
                ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
                verify(memberRepository).save(memberCaptor.capture());

                Member savedMember = memberCaptor.getValue();
                assertThat(savedMember.getName()).isEqualTo(reqDto.name());
                assertThat(savedMember.getEmail()).isEqualTo(reqDto.email());
                assertThat(savedMember.getPhoneNumber()).isEqualTo(reqDto.phoneNumber());
                assertThat(savedMember.getBirth()).isEqualTo(reqDto.birth());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 Member ID가 주어졌을 때")
        class Context_with_not_exist_memberId {
            private UpdateMemberReqDto reqDto;

            @BeforeEach
            void setUp() {
                reqDto = new UpdateMemberReqDto(
                        "홍길동",
                        "test1@example.com",
                        "010-1234-5678",
                        LocalDate.of(2000, 1, 1)
                );
                given(memberRepository.findById(1L)).willReturn(Optional.empty());
            }

            @Test
            @DisplayName("Member ID 찾을 수 없음 예외를 던진다.")
            void it_throw_memberId_exception() {
                // when & then
                assertThatThrownBy(() -> updateMemberService.execute(1L, reqDto))
                        .isInstanceOf(RuntimeException.class);

                verify(memberRepository, never()).save(any(Member.class));
            }
        }

        @Nested
        @DisplayName("중복된 Email이 주어졌을 때")
        class Context_with_exist_email {
            private UpdateMemberReqDto reqDto;

            @BeforeEach
            void setUp() {
                reqDto = new UpdateMemberReqDto(
                        "홍길동",
                        "test1@example.com",
                        "010-1234-5678",
                        LocalDate.of(2000, 1, 1)
                );
                given(memberRepository.existsByEmail(reqDto.email())).willReturn(true);
            }

            @Test
            @DisplayName("Email 중복 예외를 던진다.")
            void it_throw_email_exception() {
                // when & then
                assertThatThrownBy(() -> updateMemberService.execute(1L, reqDto))
                        .isInstanceOf(RuntimeException.class);

                verify(memberRepository, never()).save(any(Member.class));
            }
        }

        @Nested
        @DisplayName("중복된 PhoneNumber가 주어졌을 때")
        class Context_with_exist_phoneNumber {
            private UpdateMemberReqDto reqDto;

            @BeforeEach
            void setUp() {
                reqDto = new UpdateMemberReqDto(
                        "홍길동",
                        "test1@example.com",
                        "010-1234-5678",
                        LocalDate.of(2000, 1, 1)
                );
                given(memberRepository.existsByEmail(reqDto.email())).willReturn(false);
                given(memberRepository.existsByPhoneNumber(reqDto.phoneNumber())).willReturn(true);
            }

            @Test
            @DisplayName("PhoneNumber 중복 예외를 던진다.")
            void it_throw_email_exception() {
                // when & then
                assertThatThrownBy(() -> updateMemberService.execute(1L, reqDto))
                        .isInstanceOf(RuntimeException.class);

                verify(memberRepository, never()).save(any(Member.class));
            }
        }
    }
}

