package shop.zip.travel.domain.post.travelogue.dto.res;

import java.util.Objects;
import shop.zip.travel.domain.member.dto.MemberSimpleRes;
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

	private static final Long ZERO = 0L;

	public static TravelogueSimpleRes toDto(
			TravelogueSimple travelogueSimple
	) {
		long nights = travelogueSimple.period().getNights();
		Long likeCount = travelogueSimple.likeCount();

		return new TravelogueSimpleRes(
				travelogueSimple.travelogueId(),
				travelogueSimple.title(),
				nights,
				nights + 1,
				travelogueSimple.totalCost(),
				travelogueSimple.country(),
				travelogueSimple.thumbnail(),
				MemberSimpleRes.toDto(travelogueSimple),
				(Objects.isNull(likeCount)) ? ZERO : likeCount
		);
	}
}
