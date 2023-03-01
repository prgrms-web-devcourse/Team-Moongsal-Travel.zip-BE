package shop.zip.travel.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberMyPageService {

  private final MemberRepository memberRepository;

  public MemberMyPageService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public MemberInfoRes getInfoBy(Long memberId) {
    return memberRepository.getMemberInfo(memberId);
  }

}

