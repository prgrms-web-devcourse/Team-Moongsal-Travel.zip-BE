package shop.zip.travel.domain.email.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import shop.zip.travel.domain.email.exception.NotValidatedVerificationCodeException;
import shop.zip.travel.global.util.RedisUtil;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @Mock
  private JavaMailSender javaMailSender;

  @Mock
  private RedisUtil redisUtil;

  @InjectMocks
  private EmailService emailService;

  // email 보내는 메서드 테스트를 어떻게 해야할까?w
//  @Test
//  public void sendEmail_success() {
//    emailService.sendEmail("superstring77@gmail.com");
//    when(javaMailSender.createMimeMessage()).thenReturn((MimeMessage) any());
//    verify(javaMailSender).send((MimeMessage) any());
//    verify(redisUtil).setDataWithExpire("superstring77@gmail.com", "123456", 3L);
//  }

  @Test
  public void validateVerificationCode_NotMatch() {
    when(redisUtil.getData("superstring77@gmail.com")).thenReturn("123456");

    assertThatThrownBy(() -> emailService.validateVerificationCode("superstring77@gmail.com","782193"))
        .isInstanceOf(NotValidatedVerificationCodeException.class);
  }

  @Test
  public void validateVerificationCode_success() {
    when(redisUtil.getData("superstring77@gmail.com")).thenReturn("123456");

    emailService.validateVerificationCode("superstring77@gmail.com", "123456");
    verify(redisUtil).deleteData("superstring77@gmail.com");

  }
}