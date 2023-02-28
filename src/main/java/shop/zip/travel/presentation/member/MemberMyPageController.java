package shop.zip.travel.presentation.member;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.service.MemberMyPageService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/members/my")
public class MemberMyPageController {

  private final MemberMyPageService memberService;

  public MemberMyPageController(MemberMyPageService memberService) {
    this.memberService = memberService;
  }

  @GetMapping("/info")
  public ResponseEntity<MemberInfoRes> getInfo(
    @AuthenticationPrincipal UserPrincipal userPrincipal) {
    MemberInfoRes memberInfo = memberService.getInfoBy(userPrincipal.getUserId());

    return ResponseEntity.ok(memberInfo);
  }

}

