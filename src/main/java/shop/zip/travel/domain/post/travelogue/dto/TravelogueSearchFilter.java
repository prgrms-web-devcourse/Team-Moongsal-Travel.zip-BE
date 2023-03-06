package shop.zip.travel.domain.post.travelogue.dto;

import java.time.LocalDate;

public record TravelogueSearchFilter(
    LocalDate startDate,
    LocalDate endDate,
    Long lowest,
    Long maximum
) {

}

