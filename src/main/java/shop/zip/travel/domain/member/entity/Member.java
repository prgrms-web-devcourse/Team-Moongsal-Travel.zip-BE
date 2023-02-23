package shop.zip.travel.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  @Column(unique = true, nullable = false, length = 12)
  private String nickname;

  @Column(nullable = false)
  private int birthYear;

  protected Member() {

  }

  public Member(String email, String password, String nickname, int birthYear) {
    validateMember(email, password, nickname, birthYear);
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birthYear = birthYear;
  }

  public void validateMember(String email, String password, String nickname, int birthYear) {
    validateEmail(email);
    validatePassword(password);
    validateNickname(nickname);
    validateBirthYear(birthYear);
  }


  private void validateEmail(String email) {
    String emailPattern = "^[\\w-.]+@[\\w-]+.[\\w.]+$";
    Assert.isTrue(email.matches(emailPattern),"이메일이 형식에 맞지 않습니다");
  }

  private void validatePassword(String password) {
    String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*])[A-Za-z\\d~!@#$%^&*]{8,}$";
    Assert.isTrue(password.matches(passwordPattern),"비밀번호가 형식에 맞지 않습니다");
  }

  private void validateNickname(String nickname) {
    String nicknamePattern = "^[가-힣|a-zA-Z]{2,12}$";
    Assert.isTrue(nickname.matches(nicknamePattern),"닉네임이 형식에 맞지 않습니다");
  }

  private void validateBirthYear(int birthYear) {
    int currentYear = LocalDate.now().getYear();
    Assert.isTrue(currentYear - 119 <= birthYear && birthYear <= currentYear - 5, "탄생년이 올바르지 않습니다");
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

  public int getBirthYear() {
    return birthYear;
  }
}
