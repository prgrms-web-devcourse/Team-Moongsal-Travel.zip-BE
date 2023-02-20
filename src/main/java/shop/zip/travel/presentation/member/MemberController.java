package shop.zip.travel.presentation.member;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.member.dto.request.MemberSignupRequest;
import shop.zip.travel.domain.member.service.MemberService;

@RestController
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/api/auth/signup")
  public ResponseEntity<?> signup(@RequestBody @Valid MemberSignupRequest memberSignupRequest) {
    memberService.createMember(memberSignupRequest);
    return ResponseEntity.ok(null);
  }
}
