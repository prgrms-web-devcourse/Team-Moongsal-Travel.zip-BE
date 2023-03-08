package shop.zip.travel.presentation.suggestion;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.suggestion.service.SuggestionService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

  private final SuggestionService suggestionService;

  public SuggestionController(SuggestionService suggestionService) {
    this.suggestionService = suggestionService;
  }

  @GetMapping
  public ResponseEntity<TravelogueCustomSlice<TravelogueSimpleRes>> suggestion(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    suggestionService.findAll(userPrincipal.getUserId());
    return null;
  }

}
