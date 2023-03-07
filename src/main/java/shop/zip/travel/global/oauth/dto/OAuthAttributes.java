package shop.zip.travel.global.oauth.dto;

import java.util.Map;
import java.util.UUID;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.entity.Provider;
import shop.zip.travel.domain.member.entity.Role;

public class OAuthAttributes {
  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String provider;
  private String providerId;

  public String getProfileImage() {
    return profileImage;
  }

  private String profileImage;

  public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name,
      String email, String provider, String providerId, String profileImage) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.provider = provider;
    this.providerId = providerId;
    this.profileImage = profileImage;
  }

  public String getNameAttributeKey() {
    return nameAttributeKey;
  }

  public static OAuthAttributes of(String registrationId, String userNameAttributeName,
      Map<String, Object> attributes) {
    switch (registrationId) {
      case "kakao" -> {
        return ofKakao(userNameAttributeName, attributes);
      }
      default -> {
        return ofKakao(userNameAttributeName, attributes);
      }
    }
  }

  private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) account.get("profile");

    return new OAuthAttributes(attributes, userNameAttributeName, (String) profile.get("nickname"),
        (String) account.get("email"), Provider.KAKAO.name(), String.valueOf(attributes.get("id")) ,
        (String) profile.get("profile_image_url"));
  }

  public Member toEntity() {
    return new Member(email,
        UUID.randomUUID().toString(),
        this.name,
        "입력필요",
        this.profileImage,
        false,
        Role.GUEST,
        Provider.KAKAO,
        providerId
    );
  }

  public String getEmail() {
    return email;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }
}
