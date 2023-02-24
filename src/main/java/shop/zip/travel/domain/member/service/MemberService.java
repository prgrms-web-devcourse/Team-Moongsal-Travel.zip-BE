package shop.zip.travel.domain.member.service;

import static shop.zip.travel.domain.member.dto.request.MemberSignupRequest.toMember;

import org.springframework.stereotype.Service;
import shop.zip.travel.domain.member.dto.request.MemberSignupRequest;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.DuplicatedEmailException;
import shop.zip.travel.domain.member.exception.DuplicatedNicknameException;
import shop.zip.travel.domain.member.exception.NotVerifiedAuthorizationCodeException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.util.RedisUtil;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final RedisUtil redisUtil;

  public MemberService(MemberRepository memberRepository, RedisUtil redisUtil) {
    this.memberRepository = memberRepository;
    this.redisUtil = redisUtil;
  }

  public void createMember(MemberSignupRequest memberSignupRequest) {
    Member member = toMember(memberSignupRequest);
    memberRepository.save(member);
  }

  public void validateDuplicatedEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL);
    }
  }

  public void validateDuplicatedNickname(String nickname) {
    if (memberRepository.existsByNickname(nickname)) {
      throw new DuplicatedNicknameException(ErrorCode.DUPLICATED_NICKNAME);
    }
  }

  public void verifyCode(String email, String code) {
    if (redisUtil.getData(email) != null && redisUtil.getData(email).equals(code)) {
      redisUtil.deleteData(email);
    } else {
      throw new NotVerifiedAuthorizationCodeException(ErrorCode.NOT_VERIFIED_CODE);
    }
  }
}
