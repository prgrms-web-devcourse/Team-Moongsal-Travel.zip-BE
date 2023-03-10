package shop.zip.travel.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberSigninReq(
    @NotBlank
    @Email
    String email,

    @NotBlank
    String password
) {
}
