package shop.zip.travel.global.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  public CustomUserDetailsService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    return new UserPrincipal(member);
  }

  public UserDetails loadUserById(Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    return new UserPrincipal(member);
  }
}
