package shop.zip.travel.domain.post.travelogue.dto;

import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;

public record TravelogueUpdate(
    Period period,
    String title,
    Country country,
    String thumbnail,
    Cost cost
) {

}
