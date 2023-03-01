package shop.zip.travel.presentation.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.service.MemberMyPageService;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/members/my")
public class MemberMyPageController {

  private static final int DEFAULT_SIZE = 5;

  private final MemberMyPageService memberService;

  public MemberMyPageController(MemberMyPageService memberService) {
    this.memberService = memberService;
  }

  @GetMapping("/info")
  public ResponseEntity<MemberInfoRes> getMyInfo(
    @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    MemberInfoRes memberInfo = memberService.getInfoBy(userPrincipal.getUserId());

    return ResponseEntity.ok(memberInfo);
  }

  @GetMapping("/travelogues")
  public ResponseEntity<TravelogueCustomSlice<TravelogueSimpleRes>> getMyTravelogues(
      @PageableDefault(size = DEFAULT_SIZE) Pageable pageable,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TravelogueCustomSlice<TravelogueSimpleRes> travelogues =
        memberService.getTravelogues(userPrincipal.getUserId(), pageable);

    return ResponseEntity.ok(travelogues);
  }

}

