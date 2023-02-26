package shop.zip.travel.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import shop.zip.travel.global.util.RedisUtil;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final RedisUtil redisUtil;

  public EmailService(JavaMailSender javaMailSender, RedisUtil redisUtil) {
    this.javaMailSender = javaMailSender;
    this.redisUtil = redisUtil;
  }

  public MimeMessage createMail(String toAddress, String verificationCode)
      throws MessagingException, UnsupportedEncodingException {

    // TODO MessagingException, UnsupportedEncodingException -> 구체적 예외를 전환할 필요 있다
    MimeMessage message = javaMailSender.createMimeMessage();

    message.addRecipients(RecipientType.TO, toAddress);
    message.setSubject("Travel.zip 회원가입 인증 코드");

    StringBuilder msg = new StringBuilder();
    msg.append("이메일 주소 확인\n").append("아래 확인 코드를 회원가입 화면에서 입력해주세요\n");
    msg.append(verificationCode);

    message.setText(msg.toString(), "utf-8", "plain");
    message.setFrom(new InternetAddress("travelzip@naver.com", "Travel.zip"));

    return message;
  }

  public void sendMail(String toAddress)
      throws MessagingException, UnsupportedEncodingException {
    String code = createVerificationCode();
    MimeMessage message = createMail(toAddress, code);
    redisUtil.setDataWithExpire(toAddress, code, 180L);
    javaMailSender.send(message);
    // TODO 구체적 예외처리 필요
  }

  public String createVerificationCode() {
    StringBuilder code = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < 6; i++) {
      code.append(random.nextInt(10));
    }

    return code.toString();
  }
}
