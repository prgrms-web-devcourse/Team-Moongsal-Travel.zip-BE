package shop.zip.travel.domain.post.subTravelogue.dto.res;

public record SubTravelogueUpdateRes(
    Long subTravelogueId
) {

  public static SubTravelogueUpdateRes toDto(Long id) {
    return new SubTravelogueUpdateRes(id);
  }
}
