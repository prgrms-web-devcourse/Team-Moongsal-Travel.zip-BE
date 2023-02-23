package shop.zip.travel.domain.post.travelogue.dto;

import shop.zip.travel.domain.member.dto.MemberSimpleRes;

public record TravelogueSimpleRes(
	String title,
	int nights,
	int days,
	Long totalCost,
	String country,
	String thumbnail,
	MemberSimpleRes member
) {
}
