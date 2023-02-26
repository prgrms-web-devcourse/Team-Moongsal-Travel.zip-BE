package shop.zip.travel.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.zip.travel.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

}
