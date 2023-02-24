package shop.zip.travel.domain.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CodeValidateReq(
    @NotBlank @Email String email,
    @NotBlank @Pattern(regexp = "[0-9]{6}") String code) {

}
