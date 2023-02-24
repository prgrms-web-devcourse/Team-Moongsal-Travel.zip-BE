package shop.zip.travel.presentation.member;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.email.dto.request.CodeValidateReq;
import shop.zip.travel.domain.member.dto.request.MemberSignupReq;
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
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/api/valid/nickname/{nickname}")
  public ResponseEntity<Void> checkDuplicatedNickname(@PathVariable @Valid String nickname) {
    memberService.validateDuplicatedNickname(nickname);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/api/valid/code")
  public ResponseEntity<Void> validateVerificationCode(
      @RequestBody @Valid CodeValidateReq codeValidateReq) {
    memberService.verifyCode(codeValidateReq.email(), codeValidateReq.code());
    return ResponseEntity.noContent().build();
  }
}
