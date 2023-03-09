package shop.zip.travel.domain.post.travelogue.dto.res;

public record TravelogueUpdateRes(
    Long travelogueId
) {

  public static TravelogueUpdateRes toDto(Long id) {
    return new TravelogueUpdateRes(id);
  }
}
