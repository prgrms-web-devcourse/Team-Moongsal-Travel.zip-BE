package shop.zip.travel.domain.post.travelogue.dto;

import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TempTravelogueSimple(
    Travelogue travelogue,
    Long likeCount
) {

}
