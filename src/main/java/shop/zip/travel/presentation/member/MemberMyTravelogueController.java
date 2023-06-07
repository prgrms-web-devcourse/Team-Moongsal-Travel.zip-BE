package shop.zip.travel.presentation.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.member.service.MemberMyTravelogueService;
import shop.zip.travel.domain.post.travelogue.dto.res.TempTravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/members/my/travelogues")
public class MemberMyTravelogueController {

  private static final int DEFAULT_SIZE = 5;

  private final MemberMyTravelogueService memberMyTravelogueService;

  public MemberMyTravelogueController(MemberMyTravelogueService memberMyTravelogueService) {
    this.memberMyTravelogueService = memberMyTravelogueService;
  }

  @GetMapping
  public ResponseEntity<TravelogueCustomSlice<TravelogueSimpleRes>> getMyTravelogues(
      @PageableDefault(size = DEFAULT_SIZE) Pageable pageable,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TravelogueCustomSlice<TravelogueSimpleRes> travelogues =
        memberMyTravelogueService.getTravelogues(userPrincipal.getUserId(), pageable);

    return ResponseEntity.ok(travelogues);
  }

  @GetMapping("/temp")
  public ResponseEntity<TravelogueCustomSlice<TempTravelogueSimpleRes>> getTempAll(
      @PageableDefault(size = DEFAULT_SIZE) Pageable pageable,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    TravelogueCustomSlice<TempTravelogueSimpleRes> travelogueSimpleResList =
        memberMyTravelogueService.getMyTempTravelogues(userPrincipal.getUserId(), pageable);

    return ResponseEntity.ok(travelogueSimpleResList);
  }
}
