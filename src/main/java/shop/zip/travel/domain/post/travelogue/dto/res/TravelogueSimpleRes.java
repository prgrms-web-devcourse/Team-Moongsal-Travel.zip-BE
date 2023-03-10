package shop.zip.travel.domain.post.travelogue.dto.res;

import static shop.zip.travel.domain.post.data.DefaultValue.orGetStringReturnValue;

import shop.zip.travel.domain.member.dto.response.MemberSimpleRes;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;

public record TravelogueSimpleRes(
		Long travelogueId,
		String title,
		Long nights,
		Long days,
		Long totalCost,
		String country,
		String thumbnail,
		MemberSimpleRes member,
		Long likeCount
) {

	public static TravelogueSimpleRes toDto(
			TravelogueSimple travelogueSimple
	) {
		long nights = travelogueSimple.period().getNights();
		Long likeCount = travelogueSimple.likeCount();

		return new TravelogueSimpleRes(
				travelogueSimple.travelogueId(),
				orGetStringReturnValue(travelogueSimple.title()),
				nights,
				nights + 1,
				travelogueSimple.totalCost(),
				orGetStringReturnValue(travelogueSimple.country()),
				orGetStringReturnValue(travelogueSimple.thumbnail()),
				MemberSimpleRes.toDto(travelogueSimple),
				likeCount
		);
	}
}
