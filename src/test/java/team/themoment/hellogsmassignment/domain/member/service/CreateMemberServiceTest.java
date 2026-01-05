package team.themoment.hellogsmassignment.domain.member.service;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.themoment.hellogsmassignment.domain.member.dto.request.CreateMemberReqDto;
import team.themoment.hellogsmassignment.domain.member.entity.Member;
import team.themoment.hellogsmassignment.domain.member.repo.MemberRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateMemberService 클래스의")
class CreateMemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CreateMemberService createMemberService;

    private CreateMemberReqDto reqDto;

    @BeforeEach
    void setUp() {
        reqDto = new CreateMemberReqDto(
                "홍길동",
                "test@test.com",
                "01012345678",
                LocalDate.of(2009, 11, 22)
        );
    }

    @Nested
    class execute_메서드는 {

        @Nested
        @DisplayName("CreateMemberDTO 객체가 주어졌을 때")
        class when_create_member_dto_is_given {

            @BeforeEach
            void setUp() {
                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(false);
            }

            @Test
            @DisplayName("DTO 객체의 정보에 따라 Member를 생성하여 save 한다.")
            void it_saves_member_according_to_dto() {
                // when
                createMemberService.execute(reqDto);

                // then
                verify(memberRepository).existsByEmail(reqDto.getEmail());
                verify(memberRepository).existsByPhoneNumber(reqDto.getPhoneNumber());
                verify(memberRepository).save(any(Member.class));
            }
        }

        @Nested
        @DisplayName("중복된 Email이 주어졌을 때")
        class when_duplicate_email_is_given {

            @BeforeEach
            void setUp() {
                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(true);
            }

            @Test
            @DisplayName("Email 중복 예외를 던진다")
            void it_throws_exception_when_email_is_duplicated() {
                // when & then
                assertThrows(RuntimeException.class,
                        () -> createMemberService.execute(reqDto));

                verify(memberRepository, never()).save(any());
            }
        }

        @Nested
        @DisplayName("중복된 PhoneNumber가 주어졌을 때")
        class when_duplicate_phone_number_is_given {

            @BeforeEach
            void setUp() {
                when(memberRepository.existsByEmail(reqDto.getEmail()))
                        .thenReturn(false);
                when(memberRepository.existsByPhoneNumber(reqDto.getPhoneNumber()))
                        .thenReturn(true);
            }

            @Test
            @DisplayName(" PhoneNumber 중복 예외를 던진다.")
            void it_throws_exception_when_phone_number_is_duplicated() {
                // when & then
                assertThrows(RuntimeException.class,
                        () -> createMemberService.execute(reqDto));

                verify(memberRepository, never()).save(any());
            }
        }
    }
}
