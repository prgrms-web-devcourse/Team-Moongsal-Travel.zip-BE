package shop.zip.travel.domain.post.travelogue.dto;

public record TravelogueSearchFilter(
    Long minDays,
    Long maxDays,
    Long minCost,
    Long maxCost
) {

}

