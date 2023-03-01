package shop.zip.travel.domain.post.travelogue.repository;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public interface TravelogueRepository extends JpaRepository<Travelogue, Long>,
    TravelogueRepositoryQuerydsl {

	@Query("select new shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple("
			+ "t.id,t.title, t.period, t.cost.total, t.country.name, t.thumbnail, m.nickname, m.profileImageUrl) "
			+ "from Travelogue t "
			+ "inner join Member m "
			+ "on m.id = t.member.id ")
	Slice<TravelogueSimple> findAllBySlice(@Param("pageRequest") PageRequest pageRequest);

	@Query("select t "
		+ "from Travelogue t "
		+ "left join fetch t.member "
		+ "where t.id = :travelogueId")
	Optional<Travelogue> getTravelogueDetail(@Param("travelogueId") Long travelogueId);

  @Query(
		"select new shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple("
			+ "t.id, t.title, t.period, t.cost.total, t.country.name, t.thumbnail, m.nickname, m.profileImageUrl)"
			+ "from Travelogue t "
			+ "inner join t.member m "
			+ "where m.id = :memberId ")
  Slice<TravelogueSimple> getMyTravelogues(@Param("memberId") Long memberId,
    @Param("pageable") Pageable pageable);

}