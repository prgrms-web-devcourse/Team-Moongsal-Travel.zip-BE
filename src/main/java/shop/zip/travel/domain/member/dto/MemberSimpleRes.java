package shop.zip.travel.domain.member.dto;

import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;

public record MemberSimpleRes(
	String nickname,
	String profileImageUrl
	) {

	public static MemberSimpleRes toDto(TravelogueSimple travelogueSimple){
		return new MemberSimpleRes(
			travelogueSimple.memberNickname(),
			travelogueSimple.memberProfileImageUrl()
		);
	}
}
