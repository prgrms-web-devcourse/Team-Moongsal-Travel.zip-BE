package shop.zip.travel.domain.member.service;

import static shop.zip.travel.domain.member.dto.request.MemberSignupReq.toMember;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.dto.request.MemberSigninReq;
import shop.zip.travel.domain.member.dto.request.MemberSignupReq;
import shop.zip.travel.domain.member.dto.response.MemberSigninRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.DuplicatedEmailException;
import shop.zip.travel.domain.member.exception.EmailNotMatchException;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.exception.NotVerifiedAuthorizationCodeException;
import shop.zip.travel.domain.member.exception.PasswordNotMatchException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.security.JwtTokenProvider;
import shop.zip.travel.global.util.RedisUtil;

@Service
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final RedisUtil redisUtil;
  private final JwtTokenProvider jwtTokenProvider;

  public MemberService(MemberRepository memberRepository, RedisUtil redisUtil,
      JwtTokenProvider jwtTokenProvider) {
    this.memberRepository = memberRepository;
    this.redisUtil = redisUtil;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Transactional
  public void createMember(MemberSignupReq memberSignupReq) {
    Member member = toMember(memberSignupReq);
    memberRepository.save(member);
  }

  public void validateDuplicatedEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL);
    }
  }

  public boolean validateDuplicatedNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }

  public void verifyCode(String email, String code) {
    if (redisUtil.getData(email) != null && redisUtil.getData(email).equals(code)) {
      redisUtil.deleteData(email);
    } else {
      throw new NotVerifiedAuthorizationCodeException(ErrorCode.NOT_VERIFIED_CODE);
    }
  }

  @Transactional
  public MemberSigninRes login(MemberSigninReq memberSigninReq) {
    Member member = memberRepository.findByEmail(memberSigninReq.email())
      .orElseThrow(() -> new EmailNotMatchException(ErrorCode.EMAIL_NOT_MATCH));

    if (!member.getPassword().equals(memberSigninReq.password())) {
      throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    String accessToken = jwtTokenProvider.createToken(member.getId());

    return new MemberSigninRes(accessToken);
  }

  public Member getMember(Long id) {
    return memberRepository.findById(id)
      .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
  }

}

