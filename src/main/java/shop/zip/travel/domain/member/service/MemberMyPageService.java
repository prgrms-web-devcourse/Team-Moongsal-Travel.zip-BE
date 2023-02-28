package shop.zip.travel.domain.member.service;

import org.springframework.stereotype.Service;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
public class MemberMyPageService {

  private final MemberRepository memberRepository;

  public MemberMyPageService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  private void checkMemberExist(Long id) {
    if (memberRepository.existsById(id)) {
      return;
    }
    throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
  }

  public MemberInfoRes getInfoBy(Long memberId) {
    checkMemberExist(memberId);
    return memberRepository.getMemberInfo(memberId);
  }

}

