package shop.zip.travel.global.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

  private Map<String, Object> attributes;
  private Map<String, Object> attributesAccount;
  private Map<String, Object> attributesProfile;

  public KakaoUserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
    this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
  }

  @Override
  public Map<String, Object> getAttributes() {
    return this.attributes;
  }

  @Override
  public String getProviderId() {
    return this.attributes.get("id").toString();
  }

  @Override
  public String getProvider() {
    return "Kakao";
  }

  @Override
  public String getEmail() {
    return attributesAccount.get("email").toString();
  }

  @Override
  public String getName() {
    return attributesAccount.get("nickname").toString();
  }
}
