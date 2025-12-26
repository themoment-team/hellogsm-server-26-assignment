package team.themoment.hellogsmassignment.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.themoment.hellogsmassignment.domain.member.dto.request.CreateMemberReqDto;
import team.themoment.hellogsmassignment.domain.member.entity.Member;
import team.themoment.hellogsmassignment.domain.member.repo.MemberRepository;

@Service
@RequiredArgsConstructor
public class CreateMemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void execute(CreateMemberReqDto reqDto) {

        boolean isDuplicateEmail = memberRepository.existsByEmail(reqDto.email());
        if (isDuplicateEmail) {
            throw new RuntimeException();
        }

        boolean isDuplicatePhoneNumber = memberRepository.existsByPhoneNumber(reqDto.phoneNumber());
        if (isDuplicatePhoneNumber) {
            throw new RuntimeException();
        }

        Member member = Member.builder()
                .name(reqDto.name())
                .birth(reqDto.birth())
                .email(reqDto.email())
                .phoneNumber(reqDto.phoneNumber())
                .build();

        memberRepository.save(member);

    }

}
