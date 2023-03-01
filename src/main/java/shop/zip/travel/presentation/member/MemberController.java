package shop.zip.travel.presentation.member;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.email.dto.request.CodeValidateReq;
import shop.zip.travel.domain.member.dto.request.MemberSigninReq;
import shop.zip.travel.domain.member.dto.request.MemberSignupReq;
import shop.zip.travel.domain.member.dto.request.NicknameValidateReq;
import shop.zip.travel.domain.member.dto.response.MemberSigninRes;
import shop.zip.travel.domain.member.service.MemberService;

@RestController
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/api/auth/signup")
  public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignupReq memberSignupReq) {
    memberService.createMember(memberSignupReq);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/api/auth/valid/code")
  public ResponseEntity<Void> validateVerificationCode(
      @RequestBody @Valid CodeValidateReq codeValidateReq) {
    memberService.verifyCode(codeValidateReq.email(), codeValidateReq.code());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/api/auth/valid/nickname")
  public ResponseEntity<Void> checkDuplicatedNickname(
      @RequestBody @Valid NicknameValidateReq nicknameValidateReq) {
    memberService.validateDuplicatedNickname(nicknameValidateReq.nickname());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/api/auth/signin")
  public ResponseEntity<MemberSigninRes> signin(
      @RequestBody @Valid MemberSigninReq memberSigninReq) {
    MemberSigninRes memberSigninRes = memberService.login(memberSigninReq);
    return ResponseEntity.ok(memberSigninRes);
  }
}
