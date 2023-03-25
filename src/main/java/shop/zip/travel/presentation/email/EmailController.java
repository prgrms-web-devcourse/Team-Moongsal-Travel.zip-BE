package shop.zip.travel.presentation.email;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
  public ResponseEntity<String> sendMail(
      @RequestBody @Valid EmailValidateReq emailValidateReq
  ) {
      emailService.sendMail(emailValidateReq.email());
    return ResponseEntity.ok().body("이메일 발송 성공");
  }
}
