package shop.zip.travel.domain.member.dto.response;

import shop.zip.travel.domain.member.entity.Member;
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

	public static MemberSimpleRes toDto(Member member) {
		return new MemberSimpleRes(
				member.getNickname(),
				member.getProfileImageUrl()
		);
	}
}
