package shop.zip.travel.presentation.email;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.email.dto.request.CodeValidateReq;
import shop.zip.travel.domain.email.dto.request.EmailValidateReq;
import shop.zip.travel.domain.email.service.EmailService;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

  private final EmailService emailService;

  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping
  public ResponseEntity<Void> sendEmailForRegister(
      @RequestBody @Valid EmailValidateReq emailValidateReq
  ) {
      emailService.sendEmailForRegister(emailValidateReq.email());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/code")
  public ResponseEntity<Void> validateVerificationCode(
      @RequestBody @Valid CodeValidateReq codeValidateReq
  ) {
    emailService.validateVerificationCode(codeValidateReq.email(), codeValidateReq.code());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/find/password")
  public ResponseEntity<Void> sendEmailForFindingPassword(
      @RequestBody @Valid EmailValidateReq emailValidateReq
  ) {
    emailService.sendEmailForFindingPassword(emailValidateReq.email());
    return ResponseEntity.ok().build();
  }
}
