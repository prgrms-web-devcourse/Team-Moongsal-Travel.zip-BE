package shop.zip.travel.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import shop.zip.travel.domain.member.entity.Member;

public record MemberSignupReq(
    @NotBlank @Email String email,
    @NotBlank @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*])[A-Za-z\\d~!@#$%^&*]{8,}$") String password,
    @NotBlank @Pattern(regexp = "^[가-힣|a-zA-Z]{2,12}$") String nickname,
    int birthYear) {

  public static Member toMember(MemberSignupReq memberSignupReq) {
    return new Member(
        memberSignupReq.email(),
        memberSignupReq.password(),
        memberSignupReq.nickname(),
        memberSignupReq.birthYear());
  }
}
