package shop.zip.travel.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  @Query(
    "select new shop.zip.travel.domain.member.dto.response.MemberInfoRes("
      + "m.email, m.nickname, m.birthYear, m.profileImageUrl) "
      + "from Member m "
      + "where m.id = :memberId"
  )
  MemberInfoRes getMemberInfo(@Param("memberId") Long memberId);

}
