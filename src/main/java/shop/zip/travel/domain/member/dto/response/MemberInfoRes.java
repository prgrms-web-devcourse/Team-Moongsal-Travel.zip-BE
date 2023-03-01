package shop.zip.travel.domain.member.dto.response;

public record MemberInfoRes(
  String email,
  String nickname,
  String birthYear,
  String profileImageUrl
) {

}
