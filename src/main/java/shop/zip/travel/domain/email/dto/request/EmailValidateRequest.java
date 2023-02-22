package shop.zip.travel.domain.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailValidateRequest(@NotBlank @Email String email) {

}
