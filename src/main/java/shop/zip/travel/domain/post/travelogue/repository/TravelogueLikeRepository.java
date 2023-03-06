package shop.zip.travel.domain.post.travelogue.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.entity.Like;

@Repository
public interface TravelogueLikeRepository extends JpaRepository<Like, Long> {

  @Query("select l.id "
      + "from Like l "
      + "where l.member.id = :memberId and l.travelogue.id = :travelogueId")
  Optional<Long> findByMemberAndTravelogue(
      @Param("memberId") Long memberId,
      @Param("travelogueId") Long travelogueId
  );
}
