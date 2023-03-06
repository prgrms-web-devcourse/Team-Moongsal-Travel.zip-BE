package shop.zip.travel.domain.member.entity;

public enum Role {
  ROLE_USER("user"),;

  private final String key;

  Role(String key) {
    this.key = key;
  }
}
