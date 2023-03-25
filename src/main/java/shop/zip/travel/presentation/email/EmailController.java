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
  public ResponseEntity<String> sendEmail(
      @RequestBody @Valid EmailValidateReq emailValidateReq
  ) {
      emailService.sendEmail(emailValidateReq.email());
    return ResponseEntity.ok().body("이메일 발송 성공");
  }

  @PostMapping("/code")
  public ResponseEntity<Void> validateVerificationCode(
      @RequestBody @Valid CodeValidateReq codeValidateReq
  ) {
    emailService.validateVerificationCode(codeValidateReq.email(), codeValidateReq.code());
    return ResponseEntity.ok().build();
  }
}
