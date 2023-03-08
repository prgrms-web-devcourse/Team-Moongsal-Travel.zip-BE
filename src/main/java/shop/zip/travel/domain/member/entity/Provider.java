package shop.zip.travel.domain.member.entity;

public enum Provider {
  KAKAO("kakao");

  private String key;

  Provider(String key) {
    this.key = key;
  }
}
