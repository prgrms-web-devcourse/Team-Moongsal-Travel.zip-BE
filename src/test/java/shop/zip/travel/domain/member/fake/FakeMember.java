package shop.zip.travel.domain.member.fake;

import shop.zip.travel.domain.member.entity.Member;

public class FakeMember extends Member {

  private final Long id;

  public FakeMember(Long id, Member member) {
    super(member.getEmail(),
        member.getPassword(),
        member.getNickname(),
        member.getBirthYear(),
        member.getProfileImageUrl());
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
  }
}
