package shop.zip.travel.domain.post.travelogue.dto.res;

public record TravelogueCreateRes(
    Long id,
    Long nights,
    Long days
) {

  public static TravelogueCreateRes toDto(Long id, Long nights) {
    return new TravelogueCreateRes(
        id,
        nights,
        nights + 1
    );
  }
}
