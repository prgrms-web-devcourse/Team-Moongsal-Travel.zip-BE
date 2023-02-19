package shop.zip.travel.domain.member.entity;

import jakarta.persistence.*;
import shop.zip.travel.global.entity.BaseTimeEntity;


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

  @Column
  private int birthYear;

  protected Member() {

  }

  public Member(String email, String password, String nickname) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
  }

  public Member(String email, String password, String nickname, int birthYear) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birthYear = birthYear;
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
