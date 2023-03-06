package shop.zip.travel.domain.post.travelogue.dto.res;

import java.util.List;

public record TravelogueUpdateRes(
    Long travelogueId,
    List<Long> subTravelogueIds
) {

}
