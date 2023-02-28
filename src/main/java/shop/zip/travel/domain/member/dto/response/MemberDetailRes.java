package shop.zip.travel.domain.member.dto.response;

import shop.zip.travel.domain.member.entity.Member;

public record MemberDetailRes(
  Long id,
  String email,
  String password,
  String nickname,
  String birthYear,
  String profileImageUrl
) {

  public static MemberDetailRes toDto(Member member) {
    return new MemberDetailRes(
      member.getId(),
      member.getEmail(),
      member.getPassword(),
      member.getNickname(),
      member.getBirthYear(),
      member.getProfileImageUrl()
    );
  }

  public Member toMember() {
    return new Member(
      this.id,
      this.email,
      this.password,
      this.nickname,
      this.birthYear,
      this.profileImageUrl
    );
  }
}
