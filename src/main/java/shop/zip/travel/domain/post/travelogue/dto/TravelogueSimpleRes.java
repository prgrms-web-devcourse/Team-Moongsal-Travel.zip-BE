package shop.zip.travel.domain.post.travelogue.dto;

import java.time.temporal.ChronoUnit;

import shop.zip.travel.domain.member.dto.MemberSimpleRes;

public record TravelogueSimpleRes(
	String title,
	Long nights,
	Long days,
	Long totalCost,
	String country,
	String thumbnail,
	MemberSimpleRes member
) {
	public static TravelogueSimpleRes toDto(TravelogueSimple travelogueSimple){
		long nights = ChronoUnit.DAYS.between(travelogueSimple.startDate(), travelogueSimple.endDate());
		return new TravelogueSimpleRes(
			travelogueSimple.title(),
			nights,
			nights + 1,
			travelogueSimple.totalCost(),
			travelogueSimple.country(),
			travelogueSimple.thumbnail(),
			MemberSimpleRes.toDto(travelogueSimple)
		);
	}
}
