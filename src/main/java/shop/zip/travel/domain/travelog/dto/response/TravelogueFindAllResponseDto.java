package shop.zip.travel.domain.travelog.dto.response;


import shop.zip.travel.domain.travelog.entity.Travelogue;

public record TravelogueFindAllResponseDto(
    String thumbnail,
    String title
) {


  public static TravelogueFindAllResponseDto toTravelogue(Travelogue travelogue) {
    return new TravelogueFindAllResponseDto(travelogue.getThumbnail(), travelogue.getTitle());
  }
}