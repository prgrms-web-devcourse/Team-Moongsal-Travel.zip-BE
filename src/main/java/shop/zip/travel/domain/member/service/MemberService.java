package shop.zip.travel.domain.member.service;

import static shop.zip.travel.domain.member.dto.request.MemberSignupRequest.toMember;

import org.springframework.stereotype.Service;
import shop.zip.travel.domain.member.dto.request.MemberSignupRequest;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void createMember(MemberSignupRequest memberSignupRequest) {
        Member member = toMember(memberSignupRequest);
        memberRepository.save(member);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
