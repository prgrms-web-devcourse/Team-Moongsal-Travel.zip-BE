package shop.zip.travel.presentation.travelogue;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TraveloguePublishRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.service.TempTravelogueService;
import shop.zip.travel.domain.post.travelogue.service.TraveloguePublishService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/travelogues")
public class TempTravelogueController {

  private final TempTravelogueService tempTravelogueService;
  private final TraveloguePublishService traveloguePublishService;

  public TempTravelogueController(
      TempTravelogueService memberMyTravelogueService,
      TraveloguePublishService traveloguePublishService
  ) {
    this.tempTravelogueService = memberMyTravelogueService;
    this.traveloguePublishService = traveloguePublishService;
  }

  @PutMapping("/{travelogueId}")
  public ResponseEntity<TravelogueUpdateRes> updateTravelogue(
      @PathVariable Long travelogueId,
      @RequestBody @Valid TravelogueUpdateReq travelogueUpdateReq,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TravelogueUpdateRes travelogueUpdateRes =
        tempTravelogueService.updateTravelogue(
            travelogueId,
            userPrincipal.getUserId(),
            travelogueUpdateReq);

    return ResponseEntity.ok(travelogueUpdateRes);
  }

  @PutMapping("/{travelogueId}/subTravelogues/{subTravelogueId}")
  public ResponseEntity<SubTravelogueUpdateRes> updateSubTravelogue(
      @PathVariable Long travelogueId,
      @PathVariable Long subTravelogueId,
      @RequestBody @Valid SubTravelogueUpdateReq subTravelogueUpdateReq,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    SubTravelogueUpdateRes subTravelogueUpdateRes = tempTravelogueService.updateSubTravelogue(
        travelogueId, subTravelogueId, userPrincipal.getUserId(), subTravelogueUpdateReq
    );

    return ResponseEntity.ok(subTravelogueUpdateRes);
  }

  @PatchMapping("/{travelogueId}/publish")
  public ResponseEntity<TraveloguePublishRes> publish(
      @PathVariable Long travelogueId,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TraveloguePublishRes traveloguePublishRes = traveloguePublishService.publish(travelogueId,
        userPrincipal.getUserId());
    return ResponseEntity.ok(traveloguePublishRes);
  }

}
