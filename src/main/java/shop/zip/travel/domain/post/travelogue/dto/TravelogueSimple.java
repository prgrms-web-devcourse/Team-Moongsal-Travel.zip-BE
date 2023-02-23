package shop.zip.travel.domain.post.travelogue.dto;

import java.time.LocalDateTime;

public record TravelogueSimple(
	String title,
	LocalDateTime startDate,
	LocalDateTime endDate,
	Long totalCost,
	String country,
	String thumbnail,
	String memberNickname,
	String memberProfileImageUrl
) {
}
