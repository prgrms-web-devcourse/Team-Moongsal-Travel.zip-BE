package shop.zip.travel.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.LocalDate;
import org.springframework.util.Assert;
import shop.zip.travel.domain.base.BaseTimeEntity;

@Entity
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String nickname;

  @Column(nullable = false, length = 4)
  private String birthYear;

  @Column(nullable = false)
  private String profileImageUrl;

  @Column(nullable = false)
  private boolean isEmail;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column
  private String provider;

  @Lob
  private String providerId;

  protected Member() {
  }

  public Member(String email, String password, String nickname, String birthYear,
      String profileImageUrl, boolean isEmail, Role role, String provider, String providerId) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birthYear = birthYear;
    this.profileImageUrl = profileImageUrl;
    this.isEmail = isEmail;
    this.role = role;
    this.provider = provider;
    this.providerId = providerId;
  }

  public Member(String email, String password, String nickname, String birthYear) {
    validateMember(email, password, nickname, birthYear);
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birthYear = birthYear;
    this.profileImageUrl = "default";
    this.isEmail = false;
  }

  public Member(String email, String password, String nickname, String birthYear,
    String profileImageUrl) {
    validateMember(email, password, nickname, birthYear);
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birthYear = birthYear;
    this.profileImageUrl = profileImageUrl;
    this.isEmail = false;
  }

  private void validateMember(String email, String password, String nickname, String birthYear) {
    validateEmail(email);
    validateNickname(nickname);
    validateBirthYear(birthYear);
  }

  private void validateEmail(String email) {
    String emailPattern = "^[\\w-.]+@[\\w-]+.[\\w.]+$";
    Assert.isTrue(email.matches(emailPattern),"이메일이 형식에 맞지 않습니다");
  }

  private void validateNickname(String nickname) {
    String nicknamePattern = "^[가-힣|a-zA-Z]{2,12}$";
    Assert.isTrue(nickname.matches(nicknamePattern),"닉네임이 형식에 맞지 않습니다");
  }

  private void validateBirthYear(String birthYear) {
    int currentYear = LocalDate.now().getYear();
    Assert.isTrue(currentYear - 87 <= Integer.parseInt(birthYear)
        && Integer.parseInt(birthYear) <= currentYear - 7
      , "탄생년이 올바르지 않습니다");
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getNickname() {
    return nickname;
  }

  public String getBirthYear() {
    return birthYear;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public boolean isEmail() {
    return isEmail;
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void updateNickname(String nickname) {
    validateNickname(nickname);
    this.nickname = nickname;
  }

  public Member update(String nickname, String profileImageUrl) {
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
    return this;
  }
  public Role getRole() {
    return role;
  }

  public String getProvider() {
    return provider;
  }

  public String getProviderId() {
    return providerId;
  }
}
