package shop.zip.travel.domain.member.dto.response;

import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;

public record MemberSimpleRes(
		String nickname,
		String profileImageUrl
) {

	public static MemberSimpleRes toDto(TravelogueSimple travelogueSimple) {
		return new MemberSimpleRes(
				travelogueSimple.memberNickname(),
				travelogueSimple.memberProfileImageUrl()
		);
	}

	public static MemberSimpleRes toDto(TempTravelogueSimple travelogueSimple) {
		return new MemberSimpleRes(
				travelogueSimple.getMemberNickname(),
				travelogueSimple.getMemberProfileImageUrl()
		);
	}
}
