package shop.zip.travel.domain.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.dto.request.MemberPasswordChangeReq;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;

@SpringBootTest
class MemberServiceTest {

  @Autowired
  private MemberService memberService;
  
  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Test
  @DisplayName("비밀번호 변경이 성공적으로 이루어진다")
  @Transactional
  public void changePassword_success() {
    Member member = new Member("user123@gmail.com", "qwe123!@#", "Nickname", "1996");
    memberRepository.save(member);
    MemberPasswordChangeReq memberPasswordChangeReq = new MemberPasswordChangeReq(
        "user123@gmail.com", "rty456$%^");
        
    memberService.changePassword(memberPasswordChangeReq);

    Member memberByEmail = memberService.findMemberByEmail(memberPasswordChangeReq.email());
    String changedEncodedPassword = memberByEmail.getPassword();
    Assertions.assertThat(passwordEncoder.matches("rty456$%^", changedEncodedPassword))
        .isEqualTo(true);
  }
}