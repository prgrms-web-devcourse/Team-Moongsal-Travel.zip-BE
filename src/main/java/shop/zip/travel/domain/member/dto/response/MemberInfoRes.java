package shop.zip.travel.domain.member.dto.response;

import shop.zip.travel.domain.member.entity.Member;

public record MemberInfoRes(
  String email,
  String nickname,
  String birthYear,
  String profileImageUrl
) {

  public static MemberInfoRes toDto(Member member) {
    return new MemberInfoRes(
      member.getEmail(),
      member.getNickname(),
      member.getBirthYear(),
      member.getProfileImageUrl()
    );
  }
}
