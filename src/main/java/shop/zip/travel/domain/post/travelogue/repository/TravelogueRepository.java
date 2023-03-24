package shop.zip.travel.domain.post.travelogue.repository;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public interface TravelogueRepository extends JpaRepository<Travelogue, Long>,
    TravelogueRepositoryQuerydsl {

  @Query("select new shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple("
      + "t.id,t.title, t.period, t.cost.total, t.country.name, t.thumbnail, m.nickname, m.profileImageUrl, count(l)) "
      + "from Travelogue t "
      + "inner join Member m "
      + "on m.id = t.member.id "
      + "left join Like l "
      + "on l.travelogue.id = t.id "
      + "where t.isPublished = :isPublished "
      + "group by t.id ")
  Slice<TravelogueSimple> findAllBySlice(
      @Param("pageable") Pageable pageable,
      @Param("isPublished") boolean isPublished);

  @Query("select t "
      + "from Travelogue t "
      + "left join fetch t.member "
      + "where t.id = :travelogueId and t.isPublished = true")
  Optional<Travelogue> getTravelogueDetail(@Param("travelogueId") Long travelogueId);

  @Query(
      "select new shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple("
          + "t.id, t.title, t.period, t.cost.total, t.country.name, t.thumbnail, m.nickname, m.profileImageUrl, count(l))"
          + "from Travelogue t "
          + "inner join t.member m "
          + "left join Like l "
          + "on l.travelogue.id = t.id "
          + "where m.id = :memberId "
          + "and t.isPublished = :isPublished "
          + "group by t.id")
  Slice<TravelogueSimple> getMyTravelogues(
      @Param("memberId") Long memberId,
      @Param("pageable") Pageable pageable,
      @Param("isPublished") boolean isPublished);

  @Query(
      "select new shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple( "
          + "t, count(l)) "
          + "from Travelogue t "
          + "inner join t.member m "
          + "left join Like l "
          + "on l.travelogue.id = t.id "
          + "where m.id = :memberId and t.isPublished = :isPublished "
          + "group by t.id")
  Slice<TempTravelogueSimple> getMyTempTravelogues(
      @Param("memberId") Long memberId,
      @Param("pageable") Pageable pageable,
      @Param("isPublished") boolean isPublished);

}