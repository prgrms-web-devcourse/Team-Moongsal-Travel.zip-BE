package shop.zip.travel.presentation.suggestion;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;
import shop.zip.travel.domain.suggestion.service.SuggestionService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

  private static final int DEFAULT_SIZE = 5;

  private final SuggestionService suggestionService;
  private final TravelogueService travelogueService;

  public SuggestionController(SuggestionService suggestionService,
      TravelogueService travelogueService) {
    this.suggestionService = suggestionService;
    this.travelogueService = travelogueService;
  }

  @GetMapping
  public ResponseEntity<TravelogueCustomSlice<TravelogueSimpleRes>> suggestion(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PageableDefault(size = DEFAULT_SIZE) Pageable pageable) {

    String suggestionCountry = suggestionService.getTopSuggestionCountry(userPrincipal.getUserId());
    TravelogueCustomSlice<TravelogueSimpleRes> result = travelogueService.search(suggestionCountry,
        pageable);

    return ResponseEntity.ok(result);
  }

}
