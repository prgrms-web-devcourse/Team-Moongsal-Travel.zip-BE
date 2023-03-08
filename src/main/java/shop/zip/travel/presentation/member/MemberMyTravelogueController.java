package shop.zip.travel.presentation.member;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.member.service.MemberMyTravelogueService;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailForUpdateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.service.TravelogueMyTempService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/members/my/travelogues")
public class MemberMyTravelogueController {

  private static final int DEFAULT_SIZE = 5;

  private final MemberMyTravelogueService memberMyTravelogueService;
  private final TravelogueMyTempService travelogueTempService;

  public MemberMyTravelogueController(MemberMyTravelogueService memberMyTravelogueService,
      TravelogueMyTempService travelogueTempService) {
    this.memberMyTravelogueService = memberMyTravelogueService;
    this.travelogueTempService = travelogueTempService;
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
  public ResponseEntity<TravelogueCustomSlice<TravelogueSimpleRes>> getTempAll(
      @PageableDefault(size = DEFAULT_SIZE) Pageable pageable,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    TravelogueCustomSlice<TravelogueSimpleRes> travelogueSimpleResList =
        travelogueTempService.getMyTempTravelogues(userPrincipal.getUserId(), pageable);

    return ResponseEntity.ok(travelogueSimpleResList);
  }

  @GetMapping("/{travelogueId}")
  public ResponseEntity<TravelogueDetailForUpdateRes> getDetailForUpdate(
      @PathVariable Long travelogueId,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TravelogueDetailForUpdateRes travelogueUpdateDetailRes =
        memberMyTravelogueService.getTravelogueForUpdate(userPrincipal.getUserId(), travelogueId);

    return ResponseEntity.ok(travelogueUpdateDetailRes);
  }

  @PatchMapping("/{travelogueId}")
  public ResponseEntity<TravelogueUpdateRes> updateTravelogue(
      @PathVariable Long travelogueId,
      @RequestBody @Valid TravelogueUpdateReq travelogueUpdateReq,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TravelogueUpdateRes travelogueUpdateRes =
        memberMyTravelogueService.updateTravelogue(
            travelogueId,
            userPrincipal.getUserId(),
            travelogueUpdateReq);

    return ResponseEntity.ok(travelogueUpdateRes);
  }

  @PatchMapping("/{travelogueId}/subTravelogues/{subTravelogueId}")
  public ResponseEntity<SubTravelogueUpdateRes> updateSubTravelogue(
      @PathVariable Long travelogueId,
      @PathVariable Long subTravelogueId,
      @RequestBody SubTravelogueUpdateReq subTravelogueUpdateReq,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    SubTravelogueUpdateRes subTravelogueUpdateRes =
        memberMyTravelogueService.updateSubTravelogue(
            travelogueId,
            subTravelogueId,
            userPrincipal.getUserId(),
            subTravelogueUpdateReq
        );
    return ResponseEntity.ok(subTravelogueUpdateRes);
  }
}
