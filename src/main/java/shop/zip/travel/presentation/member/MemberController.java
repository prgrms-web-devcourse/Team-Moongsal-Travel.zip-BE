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
import shop.zip.travel.domain.member.exception.DuplicatedEmailException;
import shop.zip.travel.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> signup(
      @RequestBody @Valid MemberSignupReq memberSignupReq
  ) {
    memberService.createMember(memberSignupReq);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/valid/code")
  public ResponseEntity<Void> validateVerificationCode(
      @RequestBody @Valid CodeValidateReq codeValidateReq
  ) {
    memberService.verifyCode(codeValidateReq.email(), codeValidateReq.code());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/valid/nickname")
  public ResponseEntity checkDuplicatedNickname(
      @RequestBody @Valid NicknameValidateReq nicknameValidateReq
  ) {
    NicknameValidateRes nicknameValidateRes;

    try {
      memberService.validateDuplicatedNickname(nicknameValidateReq.nickname());

    } catch (DuplicatedEmailException e) {
      nicknameValidateRes = new NicknameValidateRes(true);

      return ResponseEntity.ok(nicknameValidateRes);
    }
    nicknameValidateRes = new NicknameValidateRes(false);

    return ResponseEntity.ok(nicknameValidateRes);
  }

  @PostMapping("/login")
  public ResponseEntity<MemberSigninRes> login(
      @RequestBody @Valid MemberSigninReq memberSigninReq
  ) {
    MemberSigninRes memberSigninRes = memberService.login(memberSigninReq);
    return ResponseEntity.ok(memberSigninRes);
  }

  @PostMapping("/refresh")
  public ResponseEntity<MemberSigninRes> reissueAccessToken(
      @RequestBody AccessTokenReissueReq accessTokenReissueReq
  ) {
    MemberSigninRes memberSigninRes = memberService.recreateAccessAndRefreshToken(
        accessTokenReissueReq);
    return ResponseEntity.ok(memberSigninRes);
  }
}
