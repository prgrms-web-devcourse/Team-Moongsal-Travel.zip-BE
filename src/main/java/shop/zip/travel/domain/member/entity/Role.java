package shop.zip.travel.domain.member.entity;

public enum Role {
  GUEST("ROLE_GUEST", "손님"),
  USER("ROLE_USER", "사용자"),
  ADMIN("ROLE_ADMIN", "관리자");

  private final String key;
  private final String title;

  Role(String key, String title) {
    this.key = key;
    this.title = title;
  }

  public String getKey() {
    return key;
  }

  public String getTitle() {
    return title;
  }
}
