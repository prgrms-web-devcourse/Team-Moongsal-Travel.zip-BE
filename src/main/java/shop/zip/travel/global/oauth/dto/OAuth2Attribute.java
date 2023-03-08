package shop.zip.travel.global.oauth.dto;

import java.util.HashMap;
import java.util.Map;

public class OAuth2Attribute {
  private Map<String, Object> attributes;
  private String provider;
  private String attributeKey;
  private String name;
  private String email;
  private String profileImageUrl;

  public OAuth2Attribute(Map<String, Object> attributes, String provider, String attributeKey,
      String name, String email, String profileImageUrl) {
    this.attributes = attributes;
    this.provider = provider;
    this.attributeKey = attributeKey;
    this.name = name;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
  }

//  public static OAuth2Attribute of(String registrationId, String userNameAttributeName,
//      Map<String, Object> attributes) {
//    switch (registrationId) {
//      case "kakao" -> {
//        return ofKakao(userNameAttributeName, attributes);
//      }
//      default -> {
//        return ofKakao(userNameAttributeName, attributes);
//      }
//    }
//  }

  public static OAuth2Attribute of(String provider, String attributeKey,
      Map<String, Object> attributes) {
    switch (provider) {
      case "kakao" -> {
        return ofKakao(provider, attributeKey, attributes);
      }
      default -> {
        throw new RuntimeException("정상적인 url이 아닙니다.");
      }
    }
  }

  private static OAuth2Attribute ofKakao(String provider, String attributeKey,
      Map<String, Object> attributes) {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

    return new OAuth2Attribute(
        attributes,
        provider,
        attributeKey,
        (String) profile.get("nickname"),
        (String) kakaoAccount.get("email"),
        (String) profile.get("profile_image_url")
    );
  }

  public Map<String, Object> convertToMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", this.attributes);
    map.put("provider", this.provider);
    map.put("attributeKey", this.attributeKey);
    map.put("name", this.name);
    map.put("email", this.email);
    map.put("profileImageUrl", this.profileImageUrl);

    return map;
  }
}
