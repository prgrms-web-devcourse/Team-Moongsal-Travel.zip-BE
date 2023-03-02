package shop.zip.travel.domain.member.entity;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;


class MemberTest {

  @ParameterizedTest
  @ValueSource(strings = {"123@gmail.com", "q@naver.com", "abc@yahoo.co.kr",
      "travel.zip@gmail.com"})
  @DisplayName("이메일 생성 성공")
  void validateEmail_success(String email) {
    Member member = new Member(email, "qwe123!#", "Albatross", "1996");
    assertThat(member.getEmail()).isEqualTo(email);
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"@gmail.com", "##@gmail.com", " 1@ya hoo.com"})
  @DisplayName("이메일 생성 실패")
  void validateEmail_fail(String email) {
    assertThatThrownBy(() -> new Member(email, "qwe123!@#", "Albatross", "1996"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {"qwe123!@#", "!@#$^%&a3", "a123456&"})
  @DisplayName("패스워드 생성 성공")
  void validatePassword_success(String password) {
    Member member = new Member("superstring7@gmail.com", password, "Albatross", "1996");
    assertThat(member.getPassword()).isEqualTo(password);
  }

//  @ParameterizedTest
//  @EmptySource
//  @ValueSource(strings = {"1234567a", "asdfgh!@", "236742^%", "a1@"})
//  @DisplayName("패스워드 생성 실패")
//  void validatePassword_fail(String password) {
//    assertThatThrownBy(() -> new Member("superstring7@gmail.com", password, "Albatross", "1996"))
//        .isInstanceOf(IllegalArgumentException.class);
//  }

  @ParameterizedTest
  @ValueSource(strings = {"여행", "여행앓이", "BLACK", "Albatross"})
  @DisplayName("닉네임 생성 성공")
  void validateNickname_success(String nickname) {
    Member member = new Member("superstring7@gmail.com", "qwe123!@#", nickname, "1996");
    assertThat(member.getNickname()).isEqualTo(nickname);
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"ㄱㄴ", "#$!", "예", "123"})
  @DisplayName("닉네임 생성 실패")
  void validateNickname_fail(String nickname) {
    assertThatThrownBy(() -> new Member("superstring7@gmail.com", "qwe123!@#", nickname, "1996"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1936", "1960", "2016"})
  @DisplayName("탄생년 생성 성공")
  void validateBirthYear_success(String birthYear) {
    Member member = new Member("superstring7@gmail.com", "qwe123!@#", "Albatross", birthYear);
    assertThat(member.getBirthYear()).isEqualTo(birthYear);
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "1935", "2017"})
  @DisplayName("탄생년 생성 실패")
  void validateBirthYear_fail(String birthYear) {
    assertThatThrownBy(
        () -> new Member("superstring7@gmail.com", "qwe123!@#", "Albatross", birthYear))
        .isInstanceOf(IllegalArgumentException.class);
  }
}