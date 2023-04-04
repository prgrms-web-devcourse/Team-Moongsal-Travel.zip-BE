package shop.zip.travel.domain.member.service;

import static shop.zip.travel.domain.member.dto.request.MemberRegisterReq.toMember;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.dto.request.AccessTokenReissueReq;
import shop.zip.travel.domain.member.dto.request.MemberLoginReq;
import shop.zip.travel.domain.member.dto.request.MemberRegisterReq;
import shop.zip.travel.domain.member.dto.response.MemberLoginRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.InvalidRefreshTokenException;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.exception.PasswordNotMatchException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.security.JwtTokenProvider;
import shop.zip.travel.global.util.RedisUtil;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final RedisUtil redisUtil;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  public MemberService(MemberRepository memberRepository, RedisUtil redisUtil,
      JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
    this.memberRepository = memberRepository;
    this.redisUtil = redisUtil;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(readOnly = true)
  public boolean checkDuplicatedNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }

  @Transactional
  public void createMember(MemberRegisterReq memberRegisterReq) {
    Member member = toMember(memberRegisterReq, passwordEncoder);
    memberRepository.save(member);
  }

  @Transactional
  public MemberLoginRes login(MemberLoginReq memberLoginReq) {
    Member member = findMemberByEmail(memberLoginReq.email());

    if (!passwordEncoder.matches(memberLoginReq.password(), member.getPassword())) {
      throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    String accessToken = jwtTokenProvider.createAccessToken(member.getId());
    String refreshToken = jwtTokenProvider.createRefreshToken();

    redisUtil.setDataWithExpire(String.valueOf(member.getId()), refreshToken, 120L);

    return new MemberLoginRes(accessToken, refreshToken);
  }

  // TODO accessToken 에서 memberId 를 가져오는 방법을 refactoring 해보기
  @Transactional
  public MemberLoginRes recreateAccessAndRefreshToken(AccessTokenReissueReq accessTokenReissueReq) {

    String accessToken = accessTokenReissueReq.accessToken();
    String refreshToken = accessTokenReissueReq.refreshToken();
    String memberId = jwtTokenProvider.getMemberIdUsingDecode(accessToken);

    if (jwtTokenProvider.validateRefreshToken(accessTokenReissueReq.refreshToken()) &&
        redisUtil.getData(memberId).equals(refreshToken)) {
      redisUtil.deleteData(memberId);
      String newAccessToken = jwtTokenProvider.createAccessToken(Long.parseLong(memberId));
      String newRefreshToken = jwtTokenProvider.createRefreshToken();
      redisUtil.setDataWithExpire(memberId, newRefreshToken, 120L);

      return new MemberLoginRes(newAccessToken, newRefreshToken);
    } else {
      throw new InvalidRefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
  }

  public void deleteRefreshToken(Long memberId) {
    redisUtil.deleteData(String.valueOf(memberId));
  }

  public Member getMember(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
  }

  private Member findMemberByEmail(String email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
  }
}

