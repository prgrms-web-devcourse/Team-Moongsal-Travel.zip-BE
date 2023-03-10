package shop.zip.travel.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Random;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import shop.zip.travel.global.util.RedisUtil;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final RedisUtil redisUtil;

  private static String messages = """
      메일 주소 확인
      아래 확인 코드를 회원가입 화면에서 입력해주세요 
      {1}
      """;
  private final String address = "travelzip@naver.com";
  private final long duration = 3L;
  private final int CODE_LENGTH = 6;
  private static Random random = new Random();

  public EmailService(JavaMailSender javaMailSender, RedisUtil redisUtil) {
    this.javaMailSender = javaMailSender;
    this.redisUtil = redisUtil;
  }

  private MimeMessage createMail(String toAddress, String verificationCode)
      throws MessagingException, UnsupportedEncodingException {

    MimeMessage message = javaMailSender.createMimeMessage();

    message.addRecipients(RecipientType.TO, toAddress);
    message.setSubject("Travel.zip 회원가입 인증 코드");

    String msg = MessageFormat.format(messages, verificationCode);

    message.setText(msg, "utf-8", "plain");
    message.setFrom(new InternetAddress(address, "Travel.zip"));

    return message;
  }

  public void sendMail(String toEmail)
      throws MessagingException, UnsupportedEncodingException {
    String code = createVerificationCode();
    MimeMessage message = createMail(toEmail, code);
    redisUtil.setDataWithExpire(toEmail, code, duration);
    javaMailSender.send(message);
  }

  private String createVerificationCode() {
    StringBuilder code = new StringBuilder();

    for (int i = 0; i < CODE_LENGTH; i++) {
      code.append(random.nextInt(10));
    }

    return String.valueOf(code);
  }
}
