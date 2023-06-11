package shop.zip.travel.domain.suggestion;

import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;

public record SuggestionCreateDto(
    Long travelogueId,
    String countryName,
    String title,
    String thumbnail,
    Long totalCost,
    Long memberId
) {

  public static SuggestionCreateDto of(TravelogueDetailRes travelogue, Long memberId) {
    return new SuggestionCreateDto(travelogue.id(), travelogue.country(), travelogue.title(),
        travelogue.thumbnail(), travelogue.totalCost(), memberId);
  }

}
