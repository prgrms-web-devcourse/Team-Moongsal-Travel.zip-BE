package shop.zip.travel.domain.post.travelogue.dto.res;

import java.util.List;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TravelogueDetailForUpdateRes(
    String title,
    Period period,
    Country country,
    Cost cost,
    String thumbnail,
    List<Long> subTravelogueIds
) {

  public static TravelogueDetailForUpdateRes toDto(Travelogue travelogue) {
    return new TravelogueDetailForUpdateRes(
        travelogue.getTitle(),
        travelogue.getPeriod(),
        travelogue.getCountry(),
        travelogue.getCost(),
        travelogue.getThumbnail(),
        travelogue.getSubTravelogues()
            .stream()
            .map(SubTravelogue::getId)
            .toList()
    );
  }
}
