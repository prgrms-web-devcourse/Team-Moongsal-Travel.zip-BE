package shop.zip.travel.presentation.member;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.email.dto.request.CodeValidateReq;
import shop.zip.travel.domain.member.dto.request.AccessTokenReissueReq;
import shop.zip.travel.domain.member.dto.request.MemberSigninReq;
import shop.zip.travel.domain.member.dto.request.MemberSignupReq;
import shop.zip.travel.domain.member.dto.request.NicknameValidateReq;
import shop.zip.travel.domain.member.dto.response.MemberSigninRes;
import shop.zip.travel.domain.member.dto.response.NicknameValidateRes;
import shop.zip.travel.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/auth")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignupReq memberSignupReq) {
    memberService.createMember(memberSignupReq);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/valid/code")
  public ResponseEntity<Void> validateVerificationCode(
      @RequestBody @Valid CodeValidateReq codeValidateReq) {
    memberService.verifyCode(codeValidateReq.email(), codeValidateReq.code());
    return ResponseEntity.ok().build();
  }

  @PostMapping("valid/nickname")
  public ResponseEntity<NicknameValidateRes> checkDuplicatedNickname(
      @RequestBody @Valid NicknameValidateReq nicknameValidateReq) {
    boolean isDuplicated = memberService.validateDuplicatedNickname(nicknameValidateReq.nickname());
    return ResponseEntity.ok(new NicknameValidateRes(isDuplicated));
  }

  @PostMapping("/signin")
  public ResponseEntity<MemberSigninRes> signin(
      @RequestBody @Valid MemberSigninReq memberSigninReq) {
    MemberSigninRes memberSigninRes = memberService.login(memberSigninReq);
    return ResponseEntity.ok(memberSigninRes);
  }

  @PostMapping("/reissue/refresh")
  public ResponseEntity<MemberSigninRes> reissueAccessToken(
      @RequestBody AccessTokenReissueReq accessTokenReissueReq
  ) {
    MemberSigninRes memberSigninRes = memberService.recreateAccessAndRefreshToken(
        accessTokenReissueReq);
    return ResponseEntity.ok(memberSigninRes);
  }
}
