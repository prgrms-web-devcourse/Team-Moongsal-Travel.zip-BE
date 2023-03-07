package shop.zip.travel.global.oauth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.entity.Role;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.oauth.dto.OAuth2Attribute;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

  public CustomOAuth2UserService() {
    log.info("CustomOAuth2UserService 진입");
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao
    String userNameAttributeName = userRequest.getClientRegistration() // id
        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

    OAuth2Attribute oAuth2Attribute =
        OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(Role.USER.getKey())),
        oAuth2Attribute.convertToMap(), userNameAttributeName);
  }
}

