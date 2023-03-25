package shop.zip.travel.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import shop.zip.travel.domain.email.exception.NotValidatedVerificationCodeException;
import shop.zip.travel.domain.email.exception.SendEmailException;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.util.RedisUtil;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final RedisUtil redisUtil;

  private final String MEESAGE = """
      메일 주소 확인
      아래 확인 코드를 회원가입 화면에서 입력해주세요 

      """;
  private final String ADDRESS = "travelzip@naver.com";
  private final long EXPIRED_DURATION = 3L;
  private final int CODE_LENGTH = 6;
  private final Random random = new Random();

  public EmailService(JavaMailSender javaMailSender, RedisUtil redisUtil) {
    this.javaMailSender = javaMailSender;
    this.redisUtil = redisUtil;
  }

  public void sendEmail(String toEmail) {
    String code = createVerificationCode();
    MimeMessage message = createMail(toEmail, code);
    redisUtil.setDataWithExpire(toEmail, code, EXPIRED_DURATION);
    javaMailSender.send(message);
  }

  public void validateVerificationCode(String email, String verificationCode) {
    if (!redisUtil.getData(email).equals(verificationCode)) {
      throw new NotValidatedVerificationCodeException(ErrorCode.NOT_VALIDATED_VERIFICATION_CODE);
    }
    redisUtil.deleteData(email);
  }

  private String createVerificationCode() {
    StringBuilder code = new StringBuilder();

    for (int i = 0; i < CODE_LENGTH; i++) {
      code.append(random.nextInt(10));
    }

    return code.toString();
  }

  private MimeMessage createMail(String toAddress, String verificationCode) {

    MimeMessage message = javaMailSender.createMimeMessage();

    try {
      message.addRecipients(RecipientType.TO, toAddress);
      message.setSubject("Travel.zip 회원가입 인증 코드");

      String msg = MEESAGE + verificationCode;

      message.setText(msg, "utf-8", "plain");
      message.setFrom(new InternetAddress(ADDRESS, "Travel.zip"));
    } catch (MessagingException | UnsupportedEncodingException exception) {
      throw new SendEmailException(ErrorCode.NOT_SEND_EMAIL);
    }

    return message;
  }
}