package shop.zip.travel.presentation.travelogue;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.travelogue.dto.res.LikeResultRes;
import shop.zip.travel.domain.post.travelogue.service.TravelogueLikeService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/travelogues/{travelogueId}")
public class TravelogueLikeController {

  private final TravelogueLikeService travelogueLikeService;

  public TravelogueLikeController(TravelogueLikeService travelogueLikeService) {
    this.travelogueLikeService = travelogueLikeService;
  }

  @PutMapping("/likes")
  public ResponseEntity<LikeResultRes> liking(
      @PathVariable Long travelogueId,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    LikeResultRes likeResultRes =
        travelogueLikeService.liking(userPrincipal.getUserId(), travelogueId);
    return ResponseEntity.ok(likeResultRes);
  }
}
