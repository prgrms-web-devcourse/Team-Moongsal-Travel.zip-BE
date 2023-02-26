package shop.zip.travel.presentation.email;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.email.dto.request.EmailValidateReq;
import shop.zip.travel.domain.email.service.EmailService;
import shop.zip.travel.domain.member.service.MemberService;

@RestController
public class EmailController {

  private final EmailService emailService;
  private final MemberService memberService;

  public EmailController(EmailService emailService, MemberService memberService) {
    this.emailService = emailService;
    this.memberService = memberService;
  }

  @PostMapping("/api/auth/email")
  public ResponseEntity<Void> requestVerificationCode(@RequestBody @Valid EmailValidateReq emailValidateReq)
      throws MessagingException, UnsupportedEncodingException {
    String email = emailValidateReq.email();
    memberService.validateDuplicatedEmail(email);
    emailService.sendMail(email);
    return ResponseEntity.ok().build();
  }

}
