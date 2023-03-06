package shop.zip.travel.presentation.travelogue;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCreateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TraveloguePublishRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.service.TraveloguePublishService;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/travelogues")
public class TravelogueController {

  private static final int DEFAULT_SIZE = 5;

  private final TravelogueService travelogueService;
  private final TraveloguePublishService traveloguePublishService;

  public TravelogueController(TravelogueService travelogueService,
      TraveloguePublishService traveloguePublishService) {
    this.travelogueService = travelogueService;
    this.traveloguePublishService = traveloguePublishService;
  }

  @PostMapping
  public ResponseEntity<TravelogueCreateRes> createTemp(
      @RequestBody @Valid TravelogueCreateReq tempTravelogueCreateReq,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TravelogueCreateRes travelogueCreateRes = travelogueService.save(tempTravelogueCreateReq,
        userPrincipal.getUserId());
    return ResponseEntity.ok(travelogueCreateRes);
  }

  @GetMapping("/{travelogueId}")
  public ResponseEntity<TravelogueDetailRes> get(
      @PathVariable Long travelogueId,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    TravelogueDetailRes travelogueDetail =
        travelogueService.getTravelogueDetail(travelogueId, userPrincipal.getUserId());

    return ResponseEntity.ok(travelogueDetail);
  }

  @GetMapping
  public ResponseEntity<TravelogueCustomSlice<TravelogueSimpleRes>> getAll(
      @PageableDefault(size = DEFAULT_SIZE) Pageable pageable
  ) {
    TravelogueCustomSlice<TravelogueSimpleRes> travelogueSimpleRes =
        travelogueService.getTravelogues(pageable);

    return ResponseEntity.ok(travelogueSimpleRes);
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

  @GetMapping("/search")
  public ResponseEntity<List<TravelogueSimpleRes>> search(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "lastTravelogue", required = false) Long lastTravelogue,
      @RequestParam(name = "orderType") String orderType, @RequestParam(name = "size") int size) {
    List<TravelogueSimpleRes> travelogueSimpleResList = travelogueService.search(lastTravelogue,
        keyword, orderType, size);

    return ResponseEntity.ok(travelogueSimpleResList);
  }

}