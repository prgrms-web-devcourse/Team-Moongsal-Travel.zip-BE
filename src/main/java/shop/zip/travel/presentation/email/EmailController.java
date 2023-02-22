package shop.zip.travel.presentation.email;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.email.dto.request.EmailValidateRequest;
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
  public String validateEmail(@RequestBody @Valid EmailValidateRequest emailValidateRequest)
      throws MessagingException, UnsupportedEncodingException {
    // TODO 이메일 중복 로직 추가 필요
    String code = emailService.sendMail(emailValidateRequest.email());
    return code;
  }
}
