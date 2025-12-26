package team.themoment.hellogsmassignment.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.themoment.hellogsmassignment.domain.member.dto.request.UpdateMemberReqDto;
import team.themoment.hellogsmassignment.domain.member.entity.Member;
import team.themoment.hellogsmassignment.domain.member.repo.MemberRepository;

@Service
@RequiredArgsConstructor
public class UpdateMemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void execute(Long memberId, UpdateMemberReqDto reqDto) {

        boolean isDuplicateEmail = memberRepository.existsByEmail(reqDto.email());
        if (isDuplicateEmail) {
            throw new RuntimeException();
        }

        boolean isDuplicatePhoneNumber = memberRepository.existsByPhoneNumber(reqDto.phoneNumber());
        if (isDuplicatePhoneNumber) {
            throw new RuntimeException();
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);

        member.updateMember(reqDto);
        memberRepository.save(member);

    }

}
