package shop.zip.travel.domain.post.travelogue.dto;

import shop.zip.travel.domain.post.travelogue.data.Period;

public record TravelogueSimple(
	String title,
	Period period,
	Long totalCost,
	String country,
	String thumbnail,
	String memberNickname,
	String memberProfileImageUrl
) {
}
