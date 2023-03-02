package shop.zip.travel.domain.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailValidateReq(
    @NotBlank @Email
    String email)
{
  
}
